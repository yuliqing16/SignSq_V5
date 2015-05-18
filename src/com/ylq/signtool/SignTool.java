package com.ylq.signtool;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.ylq.model.TiebaItem;

import a.breaks;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 使签到类继承CommonData，以方便使用一些公共配置信息
 * 
 * @author wolforce
 * 
 */
public class SignTool{
	
	final public static String UA_CHROME = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.152 Safari/535.19 CoolNovo/2.0.3.55";
	final public static String UA_IE8 = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0)";
	final public static String UA_BAIDU_PC = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; baidubrowser 1.x)";
	final public static String UA_BAIDU_ANDROID = "Mozilla/5.0 (Linux; U; Android 2.3.5; zh-cn; MI-ONE Plus Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) FlyFlow/2.4 Version/4.0 Mobile Safari/533.1 baidubrowser/042_1.8.4.2_diordna_458_084/imoaiX_01_5.3.2_sulP-ENO-IM/100028m";
	final public static String UA_ANDROID = "Mozilla/5.0 (Linux; U; Android 2.3.5; zh-cn; MI-ONE Plus Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	final public static String UA_IPHONE = "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko)";
	final public static int TIME_OUT = 30000;//超时时间为30秒
	final public static int RETRY_TIMES = 3;//重试次数
	
    String resultFlag = "false";
    String resultStr = "未知错误！";

    String signinUrl = "http://zhidao.baidu.com/submit/user?cm=100509";// 签到URL
    boolean isCompatibleModeOpen = false;//是否因为+8签到失败而是开启了+6签到模式

    Context context;

    /**
     * 循环签到各个贴吧
     * 
     * @param cookies
     */
    public SignTool(Context context)
    {
    	this.context = context;
    }
    public String signEachTieba(String cookies,String tieba_name,int signintype) 
    {
        return signEachTieba(SignTool.TurnToHash(cookies),tieba_name,signintype);
    }
    public String signEachTieba(HashMap<String, String> cookies,String tieba_name,int signintype) {
        String baseUrl = "http://tieba.baidu.com/f?kw=";
        String signUrl = "http://tieba.baidu.com/sign/add";
        String tbsUrl = "http://tieba.baidu.com/dc/common/tbs";
        String tiebaInfoUrl;

        /*ArrayList<String> tiebaList = getTiebaList(tiebaListStr, cookies);
        if (tiebaList.size() == 0) {
            resultFlag = "false";
            return "没有检测到任何贴吧，请执行以下操作：\n1.检查网络连接是否正常，是否在使用cmwap连接（失败率较高）\n2.在配置栏内输入需要签到的贴吧，并用中文或英文逗号隔开，如:李毅，wow，仙剑";
        }*/

        Response res;
        StringBuilder sb = new StringBuilder();

            sb.append(tieba_name);
            sb.append("吧:");
            System.out.println("签到" + tieba_name);

            // 如果签到失败就进行重试
            StringBuilder sbTemp = null;
            boolean isSignSucceed = false;// 是否签到成功
            boolean isSkip = false;// 是否跳过本贴吧
            for (int i = 0; i < RETRY_TIMES; i++) {
                isSkip = false;
                isSignSucceed = false;
                sbTemp = new StringBuilder();// 单个贴吧的StringBuilder
                try {
                    // 访问贴吧信息URL
                    // {"no":0,"error":"","data":{"user_info":{"user_id":774751123,"is_sign_in":0,"user_sign_rank":0,"sign_time":0,"cont_sign_num":0,"cout_total_sing_num":0,"is_org_disabled":0},"forum_info":{"is_on":true,"is_filter":false,"forum_info":{"forum_id":3995,"level_1_dir_name":"\u5355\u673a\u6e38\u620f"},"current_rank_info":{"sign_count":2298,"sign_rank":19,"member_count":83137,"dir_rate":"0.1"},"yesterday_rank_info":{"sign_count":2617,"sign_rank":18,"member_count":82861,"dir_rate":"0.1"},"weekly_rank_info":{"sign_count":2585,"sign_rank":19,"member_count":78709},"monthly_rank_info":{"sign_count":2472,"sign_rank":17,"member_count":73105},"level_1_dir_name":"\u6e38\u620f","level_2_dir_name":"\u5355\u673a\u6e38\u620f"}}}
                    tiebaInfoUrl = "http://tieba.baidu.com/sign/info?kw=" + URLEncoder.encode(tieba_name, "GBK");
                    res = Jsoup.connect(tiebaInfoUrl).cookies(cookies).userAgent(UA_ANDROID).referrer(baseUrl).timeout(TIME_OUT).ignoreContentType(true).method(Method.GET).execute();
                    cookies.putAll(res.cookies());

                    if (res.body().contains("\"is_sign_in\":1")) {
                        sbTemp.append("今日已签(跳过)\n");
                        // resultFlag = "true";
                        isSignSucceed = true;
                        isSkip = true;
                    } else {
                        /* 今日还没签过到 */
                        // 检查当前使用的网络类型
                        // 根据配置决定使用何种签到方式，0=>+4经验，1=>+6经验，2=>智能切换
                        if (signintype == 1) {
                            /* WiFi签到，此模式签到获得5点经验，每个贴吧需要消耗大概50KB流量 */
                            System.out.println("使用WIFI签到");
                            String encodedTiebaName = URLEncoder.encode(tieba_name, "GBK");
                            String androidTiebaUrl = "http://wapp.baidu.com/f/?kw=" + encodedTiebaName;
                            
                            //访问Android版贴吧页面
                            cookies.put("USER_JUMP", "2");//修改Cookies，强制跳转到智能版页面
                            cookies.put("novel_client_guide", "1");
                            res = Jsoup.connect(androidTiebaUrl).cookies(cookies).userAgent(UA_ANDROID).referrer(baseUrl).timeout(TIME_OUT).ignoreContentType(true).method(Method.GET).execute();
                            cookies.putAll(res.cookies());
                            String fid = FindString(res.body(),"(?<=fid=).+?(?=&amp;)").split("&")[0];
                            String tbs = FindString(res.body(),"(?<=tbs=).+?(?=&amp;)").split("&")[0];
                            
                            //先模拟客户端+8签到，出异常了再用+6签到（被贴吧拒绝签到的不算在内，如签到太快、人数过多等）
                            try {
                                // 模拟手机客户端签到，经验最大加8
                                // {"user_info":{"is_sign_in":"1","user_sign_rank":"1378","sign_time":"1378817297","cont_sign_num":"3","cout_total_sing_num":"90","sign_bonus_point":"8"},"error_code":"0","time":1378817297,"ctime":0,"logid":2897212345}
                                // {"error_code":"160003","error_msg":"\u96f6\u70b9\u65f6\u5206\uff0c\u8d76\u5728\u4e00\u5929\u4f0a\u59cb\u7b7e\u5230\u7684\u4eba\u597d\u591a\uff0c\u4eb2\u8981\u4e0d\u7b49\u51e0\u5206\u949f\u518d\u6765\u7b7e\u5427~","info":[],"time":1378915905,"ctime":0,"logid":705378205}
                                // {"error_code":"160008","error_msg":"\u4f60\u7b7e\u5f97\u592a\u5feb\u4e86\uff0c\u5148\u770b\u770b\u8d34\u5b50\u518d\u6765\u7b7e\u5427:)","info":[],"time":1378915972,"ctime":0,"logid":772575132}
                                signUrl = "http://c.tieba.baidu.com/c/c/forum/sign";
                                String BDUSS = cookies.get("BDUSS");
                                HashMap<String, String> postDatas = encryptSignData(BDUSS, fid, tieba_name, tbs);
                                res = Jsoup.connect(signUrl).data(postDatas).header("Content-Type", "application/x-www-form-urlencoded").cookies(cookies).userAgent(UA_BAIDU_ANDROID).referrer(baseUrl).timeout(TIME_OUT).ignoreContentType(true).method(Method.POST).execute();
                                isSignSucceed = analyseClientResult(sbTemp, res.parse().text());
                                //System.out.println(res.body());
                                if (isSignSucceed) {
									break;
								}
                            } catch (Exception e) {
                                //触发了兼容模式
                            	
                                isCompatibleModeOpen = true;
                                String tiebaBaseUrl = res.parse().select("#top_kit .blue_kit_left a").first().attr("href").replace("http://tieba.baidu.com/", "").replaceAll("/m\\?.+", "");
                                signUrl = "http://wapp.baidu.com/" + tiebaBaseUrl + "/sign?tbs=" + tbs + "&kw=" + encodedTiebaName + "&fid=" + fid;
                                //提交签到信息，模拟手机百度浏览器，可得6点经验
                                //{"no":0,"error":"5","data":{"msg":"5","add_sign_data":{"uinfo":{"is_sign_in":1,"user_sign_rank":60,"sign_time":1361447085,"cont_sign_num":10,"cout_total_sing_num":25},"finfo":{"forum_info":{"forum_id":721850,"forum_name":"","level_1_dir_name":"\u6e2f\u53f0\u4e1c\u5357\u4e9a\u660e\u661f"},"current_rank_info":{"sign_count":60},"level_1_dir_name":"\u5a31\u4e50\u660e\u661f","level_2_dir_name":"\u6e2f\u53f0\u4e1c\u5357\u4e9a\u660e\u661f"},"sign_version":1},"forum_sign_info_data":{"is_on":true,"is_filter":false,"sign_count":60,"sign_rank":439,"member_count":819,"generate_time":0,"dir_rate":"0.1","sign_day_count":10}}}
                                res = Jsoup.connect(signUrl).cookies(cookies).userAgent(UA_BAIDU_ANDROID).referrer(baseUrl).timeout(TIME_OUT).ignoreContentType(true).method(Method.GET).execute();
                                isSignSucceed = analyseWebAndBrowserResult(sbTemp, res.parse().text(), true);
                                System.out.println(res.body());
                                if (isSignSucceed) {
									break;
								}
                            }
                            
                        } else {
                            /* GPRS、3G签到，此模式签到获得4点经验，消耗流量不到1KB */
                            System.out.println("使用GPRS签到");
                            // 访问贴吧tbs密钥
                            // {"tbs":"534fd4f435a20ba31359288090","is_login":1}
                            res = Jsoup.connect(tbsUrl).cookies(cookies).userAgent(UA_ANDROID).referrer(baseUrl).timeout(TIME_OUT).ignoreContentType(true).method(Method.GET).execute();
                            cookies.putAll(res.cookies());
                            JSONObject jsonObj = new JSONObject(res.body());
                            String tbs = jsonObj.getString("tbs");
                            // 提交签到信息
                            // {"no":1101,"error":"\u4eb2\uff0c\u5df2\u7ecf\u6210\u529f\u7b7e\u5230\u4e86\u54e6~","data":""}
                            // {"no":0,"error":"","data":{"uinfo":{"is_sign_in":1,"user_sign_rank":332,"sign_time":1352188057,"cont_sign_num":1,"cout_total_sing_num":1},"finfo":{"forum_info":{"forum_id":59506,"forum_name":"\u4ed9\u52513","level_1_dir_name":"\u5355\u673a\u6e38\u620f"},"current_rank_info":{"sign_count":332},"level_1_dir_name":"\u6e38\u620f","level_2_dir_name":"\u5355\u673a\u6e38\u620f"},"sign_version":1}}
                            signUrl = "http://tieba.baidu.com/sign/add";
                            res = Jsoup.connect(signUrl).data("ie", "utf-8").data("kw", tieba_name).data("tbs", tbs).cookies(cookies).userAgent(UA_ANDROID).referrer(baseUrl).timeout(TIME_OUT).ignoreContentType(true).method(Method.POST).execute();
                            isSignSucceed = analyseWebAndBrowserResult(sbTemp, res.parse().text(), false);
                            //System.out.println(res.body());
                            Thread.sleep(1000);
                            if (isSignSucceed) {
								break;
							}
                        }
                    }

                } catch (IOException e) {
                    isSignSucceed = false;
                    sbTemp.append("连接失败\n");
                    e.printStackTrace();
                } catch (Exception e) {
                    isSignSucceed = false;
                    sbTemp.append("未知错误！\n");
                    e.printStackTrace();
                }
        }
        return sb.toString()+sbTemp.toString();
    }


    /**
     * 获取贴吧名字的List
     * 
     * @param cfgStr
     * @param cookies
     * @return
     */
    public static ArrayList<TiebaItem> getTiebaList(HashMap<String, String> cookies) {
        String likeTiebaUrl = "http://tieba.baidu.com/f/like/mylike?pn=";// 喜欢的贴吧列表URL
        ArrayList<TiebaItem> webNameList = new ArrayList<TiebaItem>();// 网络中获取的贴吧名
        // 从网络获取账户喜欢的贴吧列表
        for (int i = 0; i < RETRY_TIMES; i++) {
            try {
                Response res;
                res = Jsoup.connect(likeTiebaUrl).cookies(cookies).userAgent(UA_BAIDU_PC).timeout(TIME_OUT).ignoreContentType(true).method(Method.GET).execute();

                // 获取第一页的贴吧列表，省流量
                webNameList.addAll(analyseTiebaName(res.parse()));
                // 判断用户喜欢的贴吧列表是否超过一页，如果超过一页，就要翻页
                if (res.body().contains("class=\"pagination\"")) {
                    // 获取页数
                    String maxPageNoStr = res.parse().select("div.pagination a").last().attr("href").replace("/f/like/mylike?&pn=", "");
                    int maxPageNo = 1;
                    try {
                        maxPageNo = Integer.valueOf(maxPageNoStr);
                    } catch (NumberFormatException e) {
                        maxPageNo = 1;
                    }
                    // 分析第1页后的所有贴吧。因为刚才已经访问过第1页了，从第2页开始获取可节省一次页面访问，省流量，省时间
                    analyseTiebaPageList(likeTiebaUrl, webNameList, cookies, maxPageNo);
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return webNameList;
    }

    /**
     * 遍历多页贴吧，因为第一页在前面已经获取了，所以从第二页开始获取可节省一个网页的流量
     * 
     * @param likeTiebaUrl
     * @param webNameList
     * @param cookies
     * @param maxPageNo
     */
     public static void analyseTiebaPageList(String likeTiebaUrl, ArrayList<TiebaItem> webNameList, HashMap<String, String> cookies, int maxPageNo) {
        Response res;
        for (int i = 2; i <= maxPageNo; i++) {
            // 开启重试模式
            for (int j = 0; j < RETRY_TIMES; j++) {
                try {
                    res = Jsoup.connect(likeTiebaUrl + i).cookies(cookies).userAgent(UA_BAIDU_PC).timeout(TIME_OUT).referrer(likeTiebaUrl).ignoreContentType(true).method(Method.GET).execute();
                    webNameList.addAll(analyseTiebaName(res.parse()));
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 分析页面中的贴吧名
     * 
     * @param doc
     * @return
     */
    public static ArrayList<TiebaItem> analyseTiebaName(Document doc) {
        ArrayList<TiebaItem> tempNameList = new ArrayList<TiebaItem>();
        Elements trs = doc.select("div.forum_table tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements els = trs.get(i).select("td>a");//.eq(0).text();
            tempNameList.add(new TiebaItem(els.eq(0).text(), els.eq(1).text(), els.eq(2).text()));
        }
        return tempNameList;
    }


    /**
     * 加密模拟客户端签到的数据
     * 
     * @param BDUSS
     * @param fid
     * @param kw
     * @param tbs
     * @return
     */
    private HashMap<String, String> encryptSignData(String BDUSS, String fid, String kw, String tbs) {
        HashMap<String, String> postDatas = new HashMap<String, String>();
        ArrayList<String[]> dataArr = new ArrayList<String[]>();
        dataArr.add(new String[] { "BDUSS", BDUSS });
        dataArr.add(new String[] { "_client_id", "4.4.2wappc_" + System.currentTimeMillis() + "_618" });
        dataArr.add(new String[] { "_client_type", "2" });
        dataArr.add(new String[] { "_client_version", "4.4.2" });
        dataArr.add(new String[] { "_phone_imei", getIMEI(context) });
        dataArr.add(new String[] { "fid", fid });
        dataArr.add(new String[] { "kw", kw });
        dataArr.add(new String[] { "net_type", "3" });
        dataArr.add(new String[] { "tbs", tbs });

        String sign = "";
        String EncryptKey = "tiebaclient!!!";
        String tempStr = "";
        for (String[] paramArr : dataArr) {
            tempStr += paramArr[0] + "=" + paramArr[1];
        }

        try {
            sign = MD5.md5(URLDecoder.decode(tempStr, "GBK") + EncryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String[] paramArr : dataArr) {
            postDatas.put(paramArr[0], paramArr[1]);
        }
        postDatas.put("sign", sign);
        return postDatas;
    }

    /**
     * 获取手机的IMEI
     * 
     * @return String
     */
    public static String getIMEI(Context context) {
        String imei = "456156451534587";// 随机串
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    /**
     * 根据客户端签到返回的字符串分析签到的结果
     * 
     * @param sb
     * @param str
     * @return true=>签到成功，false=>签到失败
     * @throws JSONException 
     */
    private boolean analyseClientResult(StringBuilder sb, String str) throws JSONException {
        // {"user_info":{"is_sign_in":"1","user_sign_rank":"1378","sign_time":"1378817297","cont_sign_num":"3","cout_total_sing_num":"90","sign_bonus_point":"8"},"error_code":"0","time":1378817297,"ctime":0,"logid":2897212345}
        // {"error_code":"160003","error_msg":"\u96f6\u70b9\u65f6\u5206\uff0c\u8d76\u5728\u4e00\u5929\u4f0a\u59cb\u7b7e\u5230\u7684\u4eba\u597d\u591a\uff0c\u4eb2\u8981\u4e0d\u7b49\u51e0\u5206\u949f\u518d\u6765\u7b7e\u5427~","info":[],"time":1378915905,"ctime":0,"logid":705378205}
        // {"error_code":"160008","error_msg":"\u4f60\u7b7e\u5f97\u592a\u5feb\u4e86\uff0c\u5148\u770b\u770b\u8d34\u5b50\u518d\u6765\u7b7e\u5427:)","info":[],"time":1378915972,"ctime":0,"logid":772575132}
        boolean flag = false;
        JSONObject jsonObj = new JSONObject(str);
        String error_code = jsonObj.getString("error_code");
        if (error_code.equals("0")) {
            flag = true;
            sb.append("签到成功，共签");
            sb.append(jsonObj.getJSONObject("user_info").getString("cont_sign_num"));
            sb.append("次，+8分,");
            sb.append("签到排名:");
            sb.append(jsonObj.getJSONObject("user_info").getString("user_sign_rank"));
            //sb.append(jsonObj.getJSONObject("user_info").getString("sign_bonus_point"));
            sb.append("\n");
        } else {
            flag = false;
            String error_msg = jsonObj.getString("error_msg");
            sb.append(error_msg);
            sb.append("\n");
        }
//        try {
//        } catch (Exception e) {
//            flag = false;
//            sb.append("签到失败\n");
//            e.printStackTrace();
//        }
        return flag;
    }

    /**
     * 分析普通+4签到与模拟浏览器+6签到的结果
     * 
     * @param sb 拼接当前贴吧的签到记录
     * @param str 签到后返回的JSON字符串
     * @param isSix true=>+6签到，false=>+4签到
     * @return true=>签到成功，false=>签到失败
     */
    private boolean analyseWebAndBrowserResult(StringBuilder sb, String str, boolean isSix) {
        // +6签到
        // //{"no":0,"error":"5","data":{"msg":"5","add_sign_data":{"uinfo":{"is_sign_in":1,"user_sign_rank":60,"sign_time":1361447085,"cont_sign_num":10,"cout_total_sing_num":25},"finfo":{"forum_info":{"forum_id":721850,"forum_name":"","level_1_dir_name":"\u6e2f\u53f0\u4e1c\u5357\u4e9a\u660e\u661f"},"current_rank_info":{"sign_count":60},"level_1_dir_name":"\u5a31\u4e50\u660e\u661f","level_2_dir_name":"\u6e2f\u53f0\u4e1c\u5357\u4e9a\u660e\u661f"},"sign_version":1},"forum_sign_info_data":{"is_on":true,"is_filter":false,"sign_count":60,"sign_rank":439,"member_count":819,"generate_time":0,"dir_rate":"0.1","sign_day_count":10}}}
        
        // +4签到
        // {"no":1101,"error":"\u4eb2\uff0c\u5df2\u7ecf\u6210\u529f\u7b7e\u5230\u4e86\u54e6~","data":""}
        // {"no":0,"error":"","data":{"uinfo":{"is_sign_in":1,"user_sign_rank":332,"sign_time":1352188057,"cont_sign_num":1,"cout_total_sing_num":1},"finfo":{"forum_info":{"forum_id":59506,"forum_name":"\u4ed9\u52513","level_1_dir_name":"\u5355\u673a\u6e38\u620f"},"current_rank_info":{"sign_count":332},"level_1_dir_name":"\u6e38\u620f","level_2_dir_name":"\u5355\u673a\u6e38\u620f"},"sign_version":1}}
        boolean flag = false;
        try {
            JSONObject jsonObj = new JSONObject(str);
            int no = jsonObj.getInt("no");
            
            if (no == 0) {
                flag = true;
                sb.append("签到成功，共签");
                if (isSix) {
                    sb.append(jsonObj.getJSONObject("data").getJSONObject("add_sign_data").getJSONObject("uinfo").getInt("cont_sign_num"));
                    sb.append("次，+");
                    sb.append(jsonObj.getJSONObject("data").getString("msg"));
                    sb.append("\n");
                } else {
                    sb.append(jsonObj.getJSONObject("data").getJSONObject("uinfo").getInt("cont_sign_num"));
                    sb.append("次\n");
                }
            } else if (no == 1101) {
                // 今天已签到
                flag = true;
                sb.append("亲，你之前已经签过了\n");
            } else if (no == 1001) {
                sb.append("未知错误，请重新试一下\n");
                flag = false;
            } else if (no == 1002) {
                sb.append("服务器开小差了，再签一次试试~\n");
                flag = false;
            } else if (no == 1003) {
                sb.append("服务器打盹了，再签一次叫醒它\n");
                flag = false;
            } else if (no == 1006) {
                sb.append("未知错误，请重新试一下\n");
                flag = false;
            } else if (no == 1007) {
                sb.append("服务器打瞌睡了，再签一次敲醒它\n");
                flag = false;
            } else if (no == 1010) {
                sb.append("签到太频繁了点，休息片刻再来吧：）\n");
                flag = false;
            } else if (no == 1011) {
                sb.append("未知错误，请重试\n");
                flag = false;
            } else if (no == 1023) {
                sb.append("未知错误，请重试\n");
                flag = false;
            } else if (no == 1027) {
                sb.append("未知错误，请重试\n");
                flag = false;
            } else if (no == 9000) {
                sb.append("未知错误，请重试\n");
                flag = false;
            } else if (no == 1012) {
                sb.append("贴吧目录出问题啦，请到贴吧签到吧反馈\n");
                flag = false;
            } else if (no == 1100) {
                sb.append("零点时分，赶在一天伊始签到的人好多，亲要不等几分钟再来签吧~\n");
                flag = false;
            } else if (no == 1102) {
                sb.append("你签得太快了，先看看贴子再来签吧：）\n");
                flag = false;
            } else if (no == 9) {
                sb.append("你在本吧被封禁不能进行当前操作\n");
                flag = false;
            } else if (no == 4) {
                sb.append("请您登陆以后再签到哦~\n");
                flag = false;
            } else {
                sb.append("签到失败\n");
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
            sb.append("签到失败\n");
            e.printStackTrace();
        }
        return flag;
    }
    public static String FindString(String html, String panter)
    {
    	String str = "";
    	if ((html != null) && (!html.equals("")))
	    {
	      Matcher localMatcher = Pattern.compile(panter).matcher(html);
	      if (localMatcher.find())
	        str = localMatcher.group(0);
	    }
	    return str;
	}
    public static HashMap<String, String> TurnToHash(String cookie)
    {
    	HashMap<String, String> tp=new HashMap<String, String>();
    	String [] strings = cookie.split(";");
    	for (int i = 0; i < strings.length; i++) 
    	{
    		String [] pStrings = strings[i].split("=");
    		StringBuilder stringBuilder = new StringBuilder();
    		stringBuilder.append(pStrings[1]);
    		if (pStrings.length >2)
    		{
    			for (int j = 2; j < pStrings.length; j++) 
    			{
    				stringBuilder.append("=");
    				stringBuilder.append(pStrings[j]);
				}
			}
    		tp.put(pStrings[0].trim(), stringBuilder.toString().trim());
		}   	
    	return tp;
    }
}

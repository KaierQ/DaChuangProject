package main.cn.edu.sicnu.check_system.Constances;

/**
 * Created by Kaier on 2019/4/24.
 */

/**
 * 定义本程序需要使用到的一些常量
 */
public class Constances {
    private static final String IP="192.168.31.179";

    private static final String LOGIN_SERVER_URL="http://"+IP+":8080/final_project/company/manager_login";

    private static final String HOMEPAGE_SERVER_URL="http://"+IP+":8080/final_project/homepage/getHome";

    private static final String REGISTER_COMPANY_URL="http://"+IP+":8080/final_project/company/registerCompany";

    public static final int SUCCESS = 1;

    public static final int FAIL = 2;

    public static String CURRENT_CID = "";

    /**
     * 设置当前企业号
     * @param curreentCid
     */
    public static void setCurrentCid(String curreentCid) {
        CURRENT_CID = curreentCid;
    }

    /**
     * 获取当前企业号
     * @return
     */
    public static String getCurrentCid() {
        return CURRENT_CID;
    }

    /**
     * 获取登陆验证的url
     * @return
     */
    public static String getLOGIN_SERVER_URL() {
        return LOGIN_SERVER_URL;
    }

    /**
     * 获取主页地址
     * @return
     */
    public static String getHomepageServerUrl() {
        return HOMEPAGE_SERVER_URL;
    }

    /**
     * 获取注册地址
     * @return
     */
    public static String getRegisterCompanyUrl() {
        return REGISTER_COMPANY_URL;
    }

    public static final int getSuccess(){
        return SUCCESS;
    }

    public static final int getFail(){
        return FAIL;
    }
}

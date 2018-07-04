package com.ejiahe.app.license.conf;

import com.ejiahe.app.license.constant.MailConstant;
import com.ejiahe.app.license.manager.sign.source.SignMD5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取license 系统的一些相关配置信息
 * Created by focus on 2017/11/15.
 */
public class LicenseConfig {


    private static class LicenseConfigHolder{
        private static LicenseConfig instance = new LicenseConfig();
    }

    public static LicenseConfig getInstance(){
        return LicenseConfigHolder.instance;
    }

    public String fromEmail(){
        return MailConstant.FROM_EMAIL;
    }

    public String toEmail(){
        return MailConstant.TO_EMAIL;
    }

    public String mailSmtp(){
        return MailConstant.SMTP;
    }

    public String mailUserName(){
        return MailConstant.USERNAME;
    }

    public String mailPassword(){

        return MailConstant.PWD;
    }

    public String mailPort(){

        return MailConstant.PORT;
    }

    /**
     * 获取当前系统的ip
     * @return
     */
    public String getIp(){
        if(!isWindowsOS()){
            return getLinuxLocalIp();
        }

        try{
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();

        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getServiceId(){
        return new SignMD5().createMsgSign(getIp() + getMacAddress()).substring(0, 16);
    }

    /**
     * 获取mac地址
     * @return
     */
    public  String getMacAddress(){

        if(!isWindowsOS()){
            return getLinuxMacAddress();
        }
        try{
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer("");
            for(int i=0; i<mac.length; i++) {
                if(i!=0) {
                    sb.append(":");
                }
                //字节转换为整数
                int temp = mac[i]&0xff;
                String str = Integer.toHexString(temp);
                if(str.length()==1) {
                    sb.append("0"+str);
                }else {
                    sb.append(str);
                }
            }
            return sb.toString().toUpperCase();
        }catch(Exception e){

        }
        return "";
    }


    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private  String getLinuxLocalIp() {

        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        return ip;
    }


    public static String getLinuxMacAddress(){
        String mac = "";
        try
        {
            Process p = new ProcessBuilder("ifconfig").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
            {
                Pattern pat = Pattern.compile("\\b\\w+:\\w+:\\w+:\\w+:\\w+:\\w+\\b");
                Matcher mat= pat.matcher(line);
                if(mat.find())
                {
                    mac=mat.group(0);
                }
            }
            br.close();
        }
        catch (IOException e) {}
        System.out.println("本机MAC地址为:\n"+mac);

        return mac;
    }
}

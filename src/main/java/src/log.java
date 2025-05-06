package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class log {
	
	String fileNameTmp = "";
	private Date l_Date;
	private String nameSession;
	private String namePc;
	private InetAddress ip;
	private String user = "";
	private String tentative = "";
	private String pouvoir = "";

	
	public log(String tente, String login, String pouv, String path) {
		user = login;
		tentative = tente;
		pouvoir = pouv;
		fileNameTmp = path+"\\log";
		recuperationDonnee();
		LogInfo(fileNameTmp);
	}	

	public boolean tracerDataFileLog(String Msg) throws IOException{
		BufferedWriter sortie =null;
		try{
			sortie = new BufferedWriter(new FileWriter(fileNameTmp, true));
			sortie.write(Msg);
		}catch(Exception ex){
			System.out.println(ex.toString()+"\n");
		}finally{
			if(sortie != null){
				sortie.close();
			}
		}
		return true;
	}


	public void LogInfo(String className){
		DateFormat l_DateFormat  = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
		try{
			if(tentative.equals("")) {
				tracerDataFileLog("Connexion  le  "+l_DateFormat.format(l_Date) +" par l'utilisateur :  '"+ user +"'\n");
				tracerDataFileLog("-droit :  "+ pouvoir+"\n");
				tracerDataFileLog("-nom Session :  "+ nameSession+"\n");
				tracerDataFileLog("-nom PC :  "+ namePc+"\n");
				tracerDataFileLog("-Adresse Ip :  "+ip.getHostAddress()+"\n"+"\n"+"\n");
			}else {
				tracerDataFileLog(tentative+" de Connexion  le  "+l_DateFormat.format(l_Date) +" par l'utilisateur :  '"+ user +"'  avec le mot de passe :  '"+ pouvoir+"'\n");
				tracerDataFileLog("-nom Session :  "+ nameSession+"\n");
				tracerDataFileLog("-nom PC :  "+ namePc+"\n");
				tracerDataFileLog("-Adresse Ip :  "+ip.getHostAddress()+"\n"+"\n"+"\n");
			}
		}catch (IOException e){
			System.err.println("LogDebug"+e.toString()+"\n");
		}
		catch (Exception e){
			System.err.println("LogDebug"+e.toString()+"\n");
		}
	}

	private void recuperationDonnee() {
		try {
			l_Date = new Date();
			Properties p = System.getProperties();
			nameSession = p.getProperty("user.name");
			ip = InetAddress.getLocalHost();
			namePc = InetAddress.getLocalHost().getHostName() ;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

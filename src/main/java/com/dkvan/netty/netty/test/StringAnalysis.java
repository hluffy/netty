package com.dkvan.netty.netty.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dk.gis.client.ApiException;
import com.dk.gis.client.api.IBeaconApi;
import com.dk.gis.client.model.BeaconGroupInput;
import com.dk.gis.client.model.GISLocation;

public class StringAnalysis {
	public static String stringAnalysis(String[] args){
		Positioning positioning = new Positioning();
		List<IBeaconSignal> signals = new ArrayList<IBeaconSignal>();
//		positioning.setEquipmentNum("20161231");
		StringBuffer returnString  = new StringBuffer();
		returnString.append("7b");
		
		String[] strs = args;
		DataForString dataString = new DataForString();
		GPS gps = new GPS();
		LoRa lora = new LoRa();
		boolean is7b = strs[0].equals("7B");
		boolean is7d = strs[strs.length-1].equals("7D");
		if(is7b&&is7d){
//			int count = Integer.parseInt(strs[1],16)+Integer.parseInt(strs[2],16);
//			System.out.println(count);
			String count = strs[1]+strs[2];
			dataString.setJishu(count);
			returnString.append(count);
			StringBuffer sn = new StringBuffer();
			for(int i = 3;i<14;i++){
				sn.append(strs[i]);
			}
			dataString.setSn(sn.toString());
			returnString.append(sn);
			String instructions = strs[14];
			System.out.println(instructions);
			switch (instructions) {
			case "05":
				returnString.append("85");
//				int length = Integer.parseInt(strs[15]+strs[16],16);
				String length = "0003";
				dataString.setDataLength(length);
				returnString.append(length);
				String TNum = strs[17];
				System.out.println(TNum);
				int LLength = Integer.parseInt(strs[18],16);
				System.out.println(LLength);
				StringBuffer mac = new StringBuffer();
				mac.append(Integer.parseInt(strs[19],16));
				mac.append(".");
				mac.append(Integer.parseInt(strs[20],16));
				mac.append(".");
				mac.append(Integer.parseInt(strs[21],16));
				mac.append(".");
				mac.append(Integer.parseInt(strs[22],16));
				lora.setIp(mac.toString());
				positioning.setEquipmentNum(mac.toString());
				System.out.println("mac:"+mac.toString());
				int quantity = Integer.parseInt(strs[23]+strs[24],16);
				lora.setQuantity(quantity);
				
				String T = strs[25];
				System.out.println("t:"+T);
				switch (T) {
				case "05":
					positioning.setPositioningMode("IBeacon");
					System.out.println("IBeacon---------------------------------------------------------------------------");
					StringBuffer Vdatas = new StringBuffer();
					for(int i =24;i<strs.length-1;i++){
						Vdatas.append(strs[i]);
					}
					System.out.println("length:"+(Vdatas.toString().length()/50));
					List<String> lists = new ArrayList<String>();
					for(int i = 0;i<Vdatas.toString().length()/50;i++){
						String ss = Vdatas.toString().substring(i*50,i*50+50);
						lists.add(ss.substring(2));
					}
					String[] datas = (String[])lists.toArray(new String[lists.size()]);
//					String[] datas = Vdatas.toString().split(T);
					for(String s:datas){
						int dataLength = Integer.parseInt(s.substring(0, 2),16);
						int dataActiveLength = s.substring(2,s.length()).length()/2;
						if(dataLength==dataActiveLength){
							IBeaconSignal signal = new IBeaconSignal();
							String data = s.substring(2,s.length());
							String uuid = data.substring(0,32);
							signal.setUuid(uuid);
							System.out.println("uuid:"+uuid);
							int maior = Integer.parseInt(data.substring(32,36),16);
							System.out.println("maior:"+maior);
							signal.setMaior(maior);
							int minor = Integer.parseInt(data.substring(36,40),16);
							System.out.println("minor:"+minor);
							signal.setMinor(minor);
							int rssi = Integer.parseInt(data.substring(40,42),16);
							System.out.println("rssi:"+(rssi-255));
							signal.setRssi((double)(rssi-255));
							int power = Integer.parseInt(data.substring(42,44),16);
							System.out.println("power:"+power);
							signal.setPower(power);
							int bat = Integer.parseInt(data.substring(44,46),16);
							System.out.println("bat:"+bat);
							signal.setBat(bat);
							
							if(signals.size()<3){
								signals.add(signal);
							}
						}
					}
					
					IBeaconApi client =  new IBeaconApi();
					BeaconGroupInput inputGroup = new BeaconGroupInput();
					IBeacon ib1 = HsqldbUtil.getIbeacon(signals.get(0).getMinor(),signals.get(0).getUuid());
					IBeacon ib2 = HsqldbUtil.getIbeacon(signals.get(1).getMinor(),signals.get(1).getUuid());
					IBeacon ib3 = HsqldbUtil.getIbeacon(signals.get(2).getMinor(),signals.get(2).getUuid());
					inputGroup.setRssi1(signals.get(0).getRssi());
					inputGroup.setTxPower1(signals.get(0).getPower());
//					inputGroup.setTxPower1(1);
					inputGroup.setLatitude1(ib1.getLatitude());
					inputGroup.setLongitude1(ib1.getLongitude());
					inputGroup.setRssi2(signals.get(1).getRssi());
					inputGroup.setTxPower2(signals.get(1).getPower());
//					inputGroup.setTxPower2(1);
					inputGroup.setLatitude2(ib2.getLatitude());
					inputGroup.setLongitude2(ib2.getLongitude());
					inputGroup.setRssi3(signals.get(2).getRssi());
					inputGroup.setTxPower3(signals.get(2).getPower());
//					inputGroup.setTxPower3(1);
					inputGroup.setLatitude3(ib3.getLatitude());
					inputGroup.setLongitude3(ib3.getLongitude());
					
					inputGroup.setCoefficient1(59.0);//59
					inputGroup.setCoefficient2(2.0);//2
					
					System.out.println("rssi:"+inputGroup.getRssi1()+"/"+inputGroup.getRssi2()+"/"+inputGroup.getRssi3());
					System.out.println("txpower:"+inputGroup.getTxPower1()+"/"+inputGroup.getTxPower2()+"/"+inputGroup.getTxPower3());
					System.out.println("longitude:"+inputGroup.getLongitude1()+"/"+inputGroup.getLongitude2()+"/"+inputGroup.getLongitude3());
					System.out.println("latitude:"+inputGroup.getLatitude1()+"/"+inputGroup.getLatitude2()+"/"+inputGroup.getLatitude3());
					
					try {
						GISLocation location = client.obtainLocationUsingPOST(inputGroup);
						System.out.println(location.getLatitude()+":" + location.getLongitude());
						positioning.setLatitude(location.getLongitude());
						positioning.setLongitude(location.getLatitude());
						positioning.setPositioningTime(new Timestamp(System.currentTimeMillis()));
						HsqldbUtil.updatePositioning(positioning);
					} catch (ApiException e) {
						e.printStackTrace();
					}
					
					
					
					break;
					
				case "06":
					positioning.setPositioningMode("北斗");
					System.out.println("GPS---------------------------------------------------------------------------------");
					int dataLength = Integer.parseInt(strs[26],16);
					Vdatas = new StringBuffer();
					for(int i =26;i<strs.length-1;i++){
						Vdatas.append(strs[i]);
					}
					int dataActiveLength = Vdatas.toString().length()/2;
					if(dataLength==dataActiveLength){
						String data = Vdatas.toString();
						String isLocation = data.substring(0,2);
						gps.setIsLocation(isLocation);
						System.out.println("isLocation:"+isLocation);
						
						
//						Date date = new Date();
//						date.setTime(Integer.parseInt(data.substring(2,14),16));
//						System.out.println("date:"+date);
						
						
						String we = data.substring(14,16);
						double longitude = 0.0;//经度
//						String longitudeDD = data.substring(16,18);
						int longitudeDD = Integer.parseInt(data.substring(16,18),16);
//						String longitudeMM = data.substring(18,24);
//						int longitudeMM = Integer.parseInt(data.substring(18,24),16);
						int mm = Integer.parseInt(data.substring(18,20),16);
						int mm1 = Integer.parseInt(data.substring(20,22),16);
						int mm2 = Integer.parseInt(data.substring(22,24),16);
						double longitudeMM = Double.parseDouble(mm+"."+mm1+mm2)/60.0;
						longitude = longitudeDD+longitudeMM;
//						longitude = Double.parseDouble(longitudeDD+"."+longitudeMM);
//						gps.setLongitude(longitude);
						positioning.setLongitude(longitude);
						System.out.println("longitude:"+longitude);
						
//						System.out.println("longitude:"+we+":"+longitude);
						double latitude = 0.0;//纬度
						String ns = data.substring(24,26);
//						String latitudeDD = data.substring(26,28);
						int latitudeDD = Integer.parseInt(data.substring(26,28),16);
//						String latitudeMM = data.substring(28,34);
//						int latitudeMM = Integer.parseInt(data.substring(28,34),16);
						int lamm = Integer.parseInt(data.substring(28,30),16);
						int lamm1 = Integer.parseInt(data.substring(30,32),16);
						int lamm2 = Integer.parseInt(data.substring(32,34),16);
						double latitudeMM = Double.parseDouble(lamm+"."+lamm1+lamm2)/60.0;
						latitude = latitudeDD + latitudeMM;
//						latitude = Double.parseDouble(latitudeDD+"."+latitudeMM);
//						gps.setLatitude(latitude);
						positioning.setLatitude(latitude);
						System.out.println("latiude:"+latitude);
						
//						System.out.println("latitude:"+ns+":"+latitude);
						int altitude = Integer.parseInt(data.substring(34,38),16);//海拔
						System.out.println("altitude:"+altitude+"m");
						int direction = Integer.parseInt(data.substring(38,40),16);//方向:正北为0,为世纪芳香的/2
						gps.setDirection(direction);
						System.out.println("direction:"+direction);
						int speed = Integer.parseInt(data.substring(40,42),16);
						gps.setSpeed(speed);
						System.out.println("speed:"+speed+"KM/H");
						positioning.setPositioningTime(new Timestamp(System.currentTimeMillis()));
						
//						HsqldbUtil.updatePositioning(positioning);
						HsqldbUtil.addPositioning(positioning);
						
					}
					break;
					
				default:
					break;
				}
				
				break;

			case "01":
					returnString.append("81");
					dataString.setDataLength("0003");
					returnString.append("0003");
				break;
			default:
				break;
			}
			returnString.append("090101");
			returnString.append("7d");
			
		}
		return returnString.toString().toUpperCase();
	}

}

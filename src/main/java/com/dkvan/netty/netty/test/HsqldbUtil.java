package com.dkvan.netty.netty.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HsqldbUtil {
	public static void main(String[] args) {
		Connection connect = getConnection();
		System.out.println(connect);
	}
	
	public static void addPositioning(Positioning positioning){
		Connection conn = getConnection();
		PreparedStatement ps = null;
		String sql = "insert into positioning(equipment_num,longitude,latitude,positioning_mode,positioning_time) values(?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, positioning.getEquipmentNum());
			ps.setDouble(2, positioning.getLongitude());
			ps.setDouble(3, positioning.getLatitude());
			ps.setString(4, positioning.getPositioningMode());
			ps.setTimestamp(5, positioning.getPositioningTime());
			
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void updatePositioning(Positioning positioning){
		Connection connect = getConnection();
		PreparedStatement ps = null;
		String sql = "update positioning set equipment_num=?,longitude=?,latitude=?,positioning_mode=?,positioning_time=? where equipment_num=?";
		try {
			ps = connect.prepareStatement(sql);
			ps.setString(1, positioning.getEquipmentNum());
			ps.setDouble(2, positioning.getLongitude());
			ps.setDouble(3, positioning.getLatitude());
			ps.setString(4, positioning.getPositioningMode());
			ps.setTimestamp(5, positioning.getPositioningTime());
			ps.setString(6, positioning.getEquipmentNum());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(connect!=null){
				try {
					connect.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static IBeacon getIbeacon(int minor,String uuid){
		IBeacon ibeacon = new IBeacon();
		Connection connect = getConnection();
		PreparedStatement ps = null;
		String sql = "select * from IBeacon where minor = ? and uuid = ?";
		try {
			ps = connect.prepareStatement(sql);
			ps.setInt(1, minor);
			ps.setString(2, uuid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ibeacon.setUuid(uuid);
				ibeacon.setMinor(minor);
				ibeacon.setLongitude(rs.getDouble("longitude"));
				ibeacon.setLatitude(rs.getDouble("latitude"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(connect!=null){
				try {
					connect.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ibeacon;
	}
	
	public static Connection getConnection(){
		Connection connect = null;
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connect = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/","sa","");
//			connect = DriverManager.getConnection("jdbc:hsqldb:file:D:/apache-tomcat-7.0.55/lib/hsqldb/data/test","sa","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

}

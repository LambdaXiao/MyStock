package com.android.mystock.data.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.mystock.common.log.Logs;


public class DBHelper extends SQLiteOpenHelper {

	public static final String DBNAME = "eno_dx.db";//数据库名称
	public static final String TABLENAME1 = "stockMaket";//市场
	public static final String TABLENAME2 = "stock";//所有股票
	public static final String TABLENAME3 = "selfStock";//自选股
	
	public static final String TABLENAME4 = "selffont";//字体设置
	
	public static final String TABLE_CHANNEL = "channel";//资讯频道
	
	public static final String ID = "id";//资讯表相关
	public static final String NAME = "name";
	public static final String ORDERID = "orderId";
	public static final String SELECTED = "selected";
	
	private static DBHelper myDBHelper;
	private static SQLiteDatabase myDB;
	private final static int DATABASE_VERSION = 2 ;
	private final Context myContext;
	private static final String tag ="DBHelper  ========>>>>>>>>> " ;
	
	public static final DBHelper getInstanc(Context context){
		Logs.d(tag+"  myDBHelper="+myDBHelper) ;
		if(myDBHelper == null){
			myDBHelper = new DBHelper(context) ;
		}
		return myDBHelper ;
	}
	
	public DBHelper(Context context) {
		super (context, DBNAME, null, DATABASE_VERSION);
		this.myContext = context;
	}
	
	public Context getContext(){
		return myContext;
	}

	//市场表	
	String sql1 = "CREATE TABLE IF NOT EXISTS "+TABLENAME1+"(id integer primary key autoincrement, " +
			"maketCode varchar(60)," +//市场代码
			" maketName varchar(60)," +//市场名称
			"tradeTime varchar(60)," +//交易时间
			" precision integer, " +//小数精度
			"versionID integer," +//版本
			"updateTime timestamp)";//修改时间
	//所有股票
	String sql2 = "CREATE TABLE IF NOT EXISTS "+TABLENAME2+" (id integer primary key autoincrement, " +
			"stockCode varchar(60)," + //股票代码
			"stockName varchar(60)," +  //股票名称
			"stockTag varchar(60), " +  //股票拼音代码
			"maketID integer ," + //股票市场
			"stockClass integer ," + //股票类型
			"stockGroup integer ," + //股票分组
			"plateGroup integer ," +   //板块分组
			"precision integer ," +//精度
			"lotsize integer ," +//每手
			"updateTime timestamp )";//时间戳

	//自选股
	String sql3 = "CREATE TABLE IF NOT EXISTS "+TABLENAME3+" (id integer primary key autoincrement, " +
			"stockCode varchar(60)," +//股票代码 	
			"stockName varchar(60)," +  //股票名称
			"maketID integer ," + //股票市场
			"stockClass integer," + //股票类型
			"stockOrder integer," +//排序字段
			"state integer not null," +//当前状态 0表示正常 1表示待删
			"stockGroup integer ," +//分组 0表示大盘指数 1表示自选股
			"synchronous integer default 0," +//表示是否同步 0表示未同步 1表示已经同步    数据库版本为 version=3
			"userID varchar(60) ," +//用户ID  数据库版本为 version=3
			"selfGroup integer," +//用户自定义分组
			"updateTime timestamp)";//时间戳
	
	
	String sql = "create table if not exists "+TABLE_CHANNEL +
			"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			ID + " TEXT , " +
			NAME + " TEXT , " +
			ORDERID + " INTEGER , " +
			SELECTED + " SELECTED)";
	
	String sqlfont = "create table if not exists " + TABLENAME4 +
			"(id INTEGER, " +
			"font INTEGER)";
	
	
	@Override  //数据库第一次创建的时候，会执行这个方法
	public void onCreate(SQLiteDatabase db) {
		myDB = db ;
		Logs.e("DBHelper  ============>>>>  onCreate;;;") ;
		try {
			db.execSQL(sql1);    
			db.execSQL(sql2);   
			db.execSQL(sql3);
			db.execSQL(sql);
			db.execSQL(sqlfont);
			
		} catch (SQLiteException e) {
			System.out.println("数据库建表异常，请检查！");
		}

	}


	@Override  //数据库版本更新的时候，执行这个方法
	public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {	
		myDB = db ;
		Logs.e("DBHelper  ============>>>>  oldVersion="+oldVersion+"   newVersion="+newVersion) ;
			

	}

	
	public Cursor ExecQuery(String query) {
		Cursor cursor ;
		try{
			cursor = myDB.rawQuery(query, null);
		}catch(Exception e){
			e.printStackTrace() ;
			this.onCreate(myDB) ;
			cursor = myDB.rawQuery(query, null);
		}
		return cursor ;
	}

	public void ExecSQL(String query) throws SQLException {

		myDB.execSQL(query);
	}

	public long insertSQL(String table, String nullColumnHack,
			ContentValues values) throws SQLException {

		return myDB.insertOrThrow(table, nullColumnHack, values);

	}

	public int updateSQL(String table, ContentValues values,
			String whereClause, String[] whereArgs) throws SQLException {

		return myDB.update(table, values, whereClause, whereArgs);

	}

	public Cursor ExecQueryParam(String query, String[] param)
			throws SQLException {
		//myDBHelper.open();

		return myDB.rawQuery(query, param);
		// myDBHelper.close();

	}


	/**
	 * 判断某张表是否存在
	 * @param tabName 表名
	 * @return
	 */
	public boolean isTabIsExist(String tabName){
		boolean result = false;
		if(tabName == null){
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				if(count>0){
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}                
		return result;
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		if(myDB==null){
			myDB = super.getReadableDatabase();
		}
		return myDB;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if(myDB==null){
			myDB = super.getWritableDatabase();
		}
		return myDB;
	}	
	
}
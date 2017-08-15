package com.android.mystock.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理类，具备增删改查操作。 
 *      增删改 --> 操作一个sql语句，并且有返回值。 
 *      查询    --> 1. 返回一个游标类型 
 *                2. 返回一个List<Object> 
 *                3. 返回一个List<Map<String, Object>> 
 * @author zxy 
 * 时间： 2012-09-28 
 */  
public class DataBaseManager {  
  
    public DBHelper dbHelper;  
    public static DataBaseManager instance = null;  
    private SQLiteDatabase sqliteDatabase;  
  
    /** 
     * 构造函数 
     * @param context   上下文对象 
     */  
    private DataBaseManager(Context context) {  
        dbHelper = DBHelper.getInstanc(context);
        sqliteDatabase = dbHelper.getWritableDatabase();     
    }  
      
    /*** 
     * 获取本类对象实例 
     * @param context   上下文对象 
     * @return 
     */  
    public static final DataBaseManager getInstance(Context context) {  
        if (instance == null)   
            instance = new DataBaseManager(context);  
        return instance;  
    }  
      
    /** 
     * 关闭数据库 
     */  
    public void close() {  
        if(sqliteDatabase.isOpen()) sqliteDatabase.close();  
        if(dbHelper != null) dbHelper.close();  
        if(instance != null) instance = null;  
        
       
    }  
  
    /** 
     * 插入数据 
     * @param sql       执行更新操作的sql语句 
     * @param bindArgs      sql语句中的参数,参数的顺序对应占位符顺序 
     * @return  result      返回新添记录的行号，与主键id无关  
     */  
    public Long insertDataBySql(String sql, String[] bindArgs) throws Exception{  
        long result = 0;  
        if(sqliteDatabase.isOpen()){  
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);  
            if(bindArgs != null){  
                int size = bindArgs.length;  
                for(int i = 0; i < size; i++){  
                    //将参数和占位符绑定，对应  
                    statement.bindString(i+1, bindArgs[i]);  
                }  
                result = statement.executeInsert();  
                statement.close();  
            }  
        }else{  
            Log.i("info", "数据库已关闭");  
        }  
        return result;  
    }  
      
    /** 
     * 插入数据 
     * @param table         表名 
     * @param values        要插入的数据 
     * @return  result      返回新添记录的行号，与主键id无关  
     */  
    public Long insertData(String table, ContentValues values){  
        long result = 0;  
        if(sqliteDatabase.isOpen()){  
            result = sqliteDatabase.insert(table, null, values);  
        }  
        return result;  
    }  
    /** 
     * 插入数据 
     * @param table         表名
     * @return  result      返回新添记录的行号，与主键id无关  
     */  
    public Long insertData(String table, List<ContentValues> list){  
    	long result = 0;  
    	if(sqliteDatabase.isOpen()){  
    		sqliteDatabase .beginTransaction() ;
			try{
				for(int i=0;i<list.size();i++){
					ContentValues values = list.get(i) ;
					result = sqliteDatabase.insert(table, null, values);  
				}
    			sqliteDatabase.setTransactionSuccessful() ;
    		}catch(Exception e){
				e.printStackTrace() ;
			}finally{
				sqliteDatabase.endTransaction() ;
			}
    			
    	}  
    	return result;  
    }  
      
    /** 
     * 更新数据 
     * @param sql       执行更新操作的sql语句 
     * @param bindArgs  sql语句中的参数,参数的顺序对应占位符顺序 
     */  
    public void updateDataBySql(String sql, String[] bindArgs) throws Exception{  
        if(sqliteDatabase.isOpen()){  
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);  
            if(bindArgs != null){  
                int size = bindArgs.length;  
                for(int i = 0; i < size; i++){  
                    statement.bindString(i+1, bindArgs[i]);  
                }  
                statement.execute();  
                statement.close();  
            }  
        }else{  
            Log.i("info", "数据库已关闭");  
        }  
    }  
      
    /** 
     * 更新数据 
     * @param table         表名 
     * @param values        表示更新的数据 
     * @param whereClause   表示SQL语句中条件部分的语句 
     * @param whereArgs     表示占位符的值 
     * @return 
     */  
    public int updataData(String table, ContentValues values, String whereClause, String[] whereArgs){  
        int result = 0;  
        if(sqliteDatabase.isOpen()){  
            result = sqliteDatabase.update(table, values, whereClause, whereArgs);  
        }  
        return result;  
    }  
  
    /** 
     * 删除数据 
     * @param sql       执行更新操作的sql语句 
     * @param bindArgs  sql语句中的参数,参数的顺序对应占位符顺序 
     */  
    public void deleteDataBySql(String sql, String[] bindArgs) throws Exception{  
        if(sqliteDatabase.isOpen()){  
            SQLiteStatement statement = sqliteDatabase.compileStatement(sql);  
            if(bindArgs != null){  
                int size = bindArgs.length;  
                for(int i = 0; i < size; i++){  
                    statement.bindString(i+1, bindArgs[i]);  
                }  
                Method[] mm = statement.getClass().getDeclaredMethods();  
                for (Method method : mm) {  
                    Log.i("info", method.getName());          
                    /** 
                     *  反射查看是否能获取executeUpdateDelete方法 
                     *  查看源码可知 executeUpdateDelete是public的方法，但是好像被隐藏了所以不能被调用， 
                     *      利用反射貌似只能在root以后的机器上才能调用，小米是可以，其他机器却不行，所以还是不能用。 
                     */  
                }  
                statement.execute();      
                statement.close();  
            }  
        }else{  
            Log.i("info", "数据库已关闭");  
        }  
    }  
  
    /** 
     * 删除数据 
     * @param table         表名 
     * @param whereClause   表示SQL语句中条件部分的语句 
     * @param whereArgs     表示占位符的值 
     * @return               
     */  
    public int deleteData(String table, String whereClause, String[] whereArgs){  
        int result = 0;  
        if(sqliteDatabase.isOpen()){  
            result = sqliteDatabase.delete(table, whereClause, whereArgs);  
        }  
        return result;  
    }  
      
    /** 
     * 查询数据
     * @param selectionArgs     查询条件 
     * @return                  返回查询的游标，可对数据自行操作，需要自己关闭游标 
     */  
    public Cursor queryData2Cursor(String sql, String[] selectionArgs) throws Exception{  
        if(sqliteDatabase.isOpen()){  
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);  
            if (cursor != null) {  
                cursor.moveToFirst();  
            }  
            return cursor;  
        }  
        return null;  
    }  
      
    /** 
     * 查询数据 
     * @param sql               执行查询操作的sql语句     
     * @param selectionArgs     查询条件 
     * @param object                Object的对象 
     * @return List<Object>       返回查询结果   
     */  
    public List<Object> queryData2Object(String sql, String[] selectionArgs, Object object) throws Exception{  
        List<Object> mList = new ArrayList<Object>();  
        if(sqliteDatabase.isOpen()){  
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);  
            Field[] f;  
            if(cursor != null && cursor.getCount() > 0) {  
                while(cursor.moveToNext()){  
                    f = object.getClass().getDeclaredFields();  
                    for(int i = 0; i < f.length; i++) {  
                        //为JavaBean 设值  
                        invokeSet(object, f[i].getName(), cursor.getString(cursor.getColumnIndex(f[i].getName())));  
                    }  
                    mList.add(object);  
                }  
            }  
            cursor.close();  
        }else{  
            Log.i("info", "数据库已关闭");  
        }  
        return mList;  
    }  
      
    /** 
     * 查询数据 
     * @param sql                           执行查询操作的sql语句     
     * @param selectionArgs                 查询条件 
     * @param object                            Object的对象 
     * @return  List<Map<String, Object>>   返回查询结果   
     * @throws Exception 
     */  
    public List<Map<String, Object>> queryData2Map(String sql, String[] selectionArgs, Object object)throws Exception{  
        List<Map<String, Object>> mList = new ArrayList<Map<String,Object>>();  
        if(sqliteDatabase.isOpen()){  
            Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);  
            Field[] f;  
            Map<String, Object> map;  
            if(cursor != null && cursor.getCount() > 0) {  
                while(cursor.moveToNext()){  
                    map = new HashMap<String, Object>();  
                    f = object.getClass().getDeclaredFields();  
                    for(int i = 0; i < f.length; i++) {  
                        map.put(f[i].getName(), cursor.getString(cursor.getColumnIndex(f[i].getName())));  
                    }  
                    mList.add(map);  
                }  
            }  
            cursor.close();  
        }else{  
            Log.i("info", "数据库已关闭");  
        }  
        return mList;  
    }   
      
    /**     
     * java反射bean的set方法     
     * @param objectClass     
     * @param fieldName     
     * @return     
     */         
    @SuppressWarnings("unchecked")         
    public static Method getSetMethod(Class objectClass, String fieldName) {         
        try {         
            Class[] parameterTypes = new Class[1];         
            Field field = objectClass.getDeclaredField(fieldName);         
            parameterTypes[0] = field.getType();         
            StringBuffer sb = new StringBuffer();         
            sb.append("set");         
            sb.append(fieldName.substring(0, 1).toUpperCase());         
            sb.append(fieldName.substring(1));         
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);         
            return method;         
        } catch (Exception e) {         
            e.printStackTrace();         
        }         
        return null;         
    }         
        
    /**     
     * 执行set方法     
     * @param object    执行对象     
     * @param fieldName 属性     
     * @param value     值     
     */         
    public static void invokeSet(Object object, String fieldName, Object value) {         
        Method method = getSetMethod(object.getClass(), fieldName);         
        try {         
            method.invoke(object, new Object[] { value });         
        } catch (Exception e) {         
            e.printStackTrace();         
        }         
    }   
    
    public DBHelper getDBHelper()
    {
//    	try{
//    		dbHelper.open();
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
    	return dbHelper;
    }
    
   /**
    * 自选股数据库    根据 code,market ,selfGroup查询股票是否存在
    * @param code
    * @param market
    * @return
    */
    public boolean queryStock(String code,String market){
    	boolean  isExit =false;
    	if(sqliteDatabase.isOpen()){
    		String selection[] =new String[]{code,market} ;
    		String where = "stockCode= ? and  maketID = ?" ;
    		Cursor cursor=sqliteDatabase.query(DBHelper.TABLENAME3,null, where,selection, null, null, null) ;
    		
    		isExit=cursor.moveToFirst() ;
    		//if(isExit){
    			//System.out.println("--------------------->>>>  查询结果为:"+cursor.getCount()+"   "+cursor.getString(cursor.getColumnIndex("stockCode"))+"."+cursor.getInt(cursor.getColumnIndex("maketID"))) ;
    		//}else
    			//System.out.println("--------------------->>>>  查询结果为空") ;
    	}
    	return isExit ;
    }
    
   /**
    * 自选股数据库   批量删除数据
    * @param list   ContentValues 中的key为stockCode，market ;
    * @param selfGroup 数据库的分组  0=自选，100=历史浏览记录
    */
   public boolean delectMyStockBatch(List<ContentValues> list ,String selfGroup){
	   boolean isOk =false ;
	   String code ="";
	   String market ="";
	   String whereClause = "stockCode= ? and  maketID = ? and selfGroup=?" ;
	   if(sqliteDatabase.isOpen()){
		   sqliteDatabase.beginTransaction() ;
		   try{
			   for(int i=0;i<list.size();i++){
				   ContentValues content = list.get(i) ;
				   code = content.getAsString("stockCode") ;
				   market = content.getAsInteger("market")+"" ;
				   //System.out.println("------->>>>"+whereClause+"   =="+code) ;
				   sqliteDatabase.delete(DBHelper.TABLENAME3, whereClause, new String[]{code,market,selfGroup}) ;
			   }
			   sqliteDatabase.setTransactionSuccessful() ;
			   isOk = true ;
		   }catch (Exception e) {
			   e.printStackTrace() ;
		   }finally{
			   sqliteDatabase.endTransaction() ;
		   }
	   }
	   return isOk ;
   }
   /**
    * 自选股数据库   删除一条数据
    * @param selfGroup
    */
   public void delectMyStock(String code,int market,String selfGroup){
	  
	   String whereClause = "stockCode= ? and  maketID = ? and selfGroup=?" ;
	   String whereArgs[] =new String[]{code,market+"",selfGroup} ;
	   if(sqliteDatabase.isOpen()){
		   sqliteDatabase.delete(DBHelper.TABLENAME3, whereClause, whereArgs) ;
	   }
   }
   /**
    * 自选股数据库 更新
    * @param code 股票代码
    * @param market 市场
    * @param selfGroup 数据库的分组  0=自选，100=历史浏览记录
    * @param values 更新的内容为ContentValues对象 其中的key值 与 DBHelper.TABLENAME3 相对应
    */
   public boolean updateMyStock(String code,int market,String selfGroup ,ContentValues values){
	   int result =0;
	   String whereClause = "stockCode= ? and  maketID = ? and selfGroup=?" ;
	   if(sqliteDatabase.isOpen()){
		   result =sqliteDatabase.update(DBHelper.TABLENAME3, values, whereClause, new String[]{code,market+"",selfGroup}) ;
		   //System.out.println("------------------- updateMyStock=="+result); 
	   }
	   return result>0?true:false ;
   }
   /**
    * 自选股数据库  批量更新
    * @param list ContentValues ，包含自选股中需要更新的字段，是需要更新的内容， 更新的内容为ContentValues对象 其中的key值 与 DBHelper.TABLENAME3 相对应
    * @param selfGroup 数据库的分组  0=自选，100=历史浏览记录
    * @return
    */
   public boolean updateMyStockBatch(List<ContentValues> list ,String selfGroup){
	   boolean isOk=false ;
	   String code ="";
	   String market ="";
	   String whereClause = "stockCode= ? and  maketID = ? and selfGroup=?" ;
	   if(sqliteDatabase.isOpen()){
		   sqliteDatabase.beginTransaction() ;
		   try{
			   for(int i=0;i<list.size();i++){
				   ContentValues values = list.get(i) ;
				   code = values.getAsString("stockCode") ;
				   market = values.getAsInteger("maketID") +"";
				   //System.out.println("------------->>>  "+code+"   "+values.getAsString("updateTime")) ;
				   sqliteDatabase.update(DBHelper.TABLENAME3, values, whereClause, new String[]{code,market+"",selfGroup}) ;
			   }
			   sqliteDatabase.setTransactionSuccessful() ;
			   isOk =true ;
		   }catch (Exception e) {
			   e.printStackTrace() ;
		   }finally{
			sqliteDatabase.endTransaction() ;
		   }
	   }
	   //System.out.println("------------------- updateMyStockBatch=="+isOk); 
	   return isOk ;
   }
   
   
}  
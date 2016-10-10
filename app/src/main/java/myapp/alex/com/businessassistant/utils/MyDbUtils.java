package myapp.alex.com.businessassistant.utils;

import java.io.File;
import java.util.HashMap;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.DbUtils.DaoConfig;

public class MyDbUtils {
	
	private static MyDbUtils utils=null;
	private DbUtils db;
	/**数据库缓存*/
	private HashMap<String, DbUtils> dbMap = new HashMap<String, DbUtils>();
	
	public static MyDbUtils getInstance(){
		if(utils==null){
			utils=new MyDbUtils();
		}
		return utils;
	}
	
	/**获得数据库访问工具*/
	public DbUtils Db(Context context){
		if(db == null)
		{
			// 初始化数据库
			DaoConfig c = new DaoConfig(context);
			c.setDbName("BADB");
			c.setDbVersion(1);
			c.setSdCardPath(Environment.getExternalStorageDirectory()+"/BusinessAssistant/"+".db/");
			db = getDb(c);
		}
		return db;
	}
	
	/**
	 * 获取xUtils db数据库操作工具
	 * @param config 数据库配置对象
	 * @return
	 */
	public DbUtils getDb(DaoConfig config){
		if (config == null) {
			throw new IllegalArgumentException("daoConfig may not be null");
		}
		// 保存在sd卡目录
		String dbDirs = config.getSdCardPath();
		String key = TextUtils.isEmpty(dbDirs)  ? config.getDbName() : dbDirs + config.getDbName();
		if (dbMap.containsKey(key)) {
			return dbMap.get(key);
		}

		DbUtils db;
		if(!TextUtils.isEmpty(dbDirs)){	
			// 创建文件夹
			File file = new File(dbDirs);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		// 创建数据库
		db = DbUtils.create(config);
		db.configDebug(false);
		db.configAllowTransaction(true);
		// 加入缓存
		dbMap.put(key, db);
		
		return db;
	}

}

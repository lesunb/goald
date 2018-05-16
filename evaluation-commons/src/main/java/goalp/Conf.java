package goalp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Conf {

	static Map<Keys, String> confs = new HashMap<>();
	
	static final String BASE_DIR = "." + System.getProperty("file.separator")
	+"result" + System.getProperty("file.separator");
	
	public enum Keys {
		
		@SuppressWarnings("deprecation")
		RESULT_FILE (BASE_DIR+"restult_"+(new Date()).toLocaleString()),
		DEPL_PLAN_FILE (BASE_DIR+"depl_plan_"+(new Date()).toLocaleString());

		String _default;

		Keys(String _default){
			this._default = _default;
		}
		
		public String getDefault(){
			return this._default;
		}
	}
	
	public static String get(Keys key){
		String value = confs.get(key);
		if(value == null){
			value = key.getDefault();
		}
		return value;
	}
}

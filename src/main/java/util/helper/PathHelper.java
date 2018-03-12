
package util.helper;

import org.springframework.stereotype.Service;

@Service("pathHelper")
public class PathHelper {

	
	/**
	 * 获取classPath
	 * 
	 * @param 
	 *            
	 * @return String 
	 */
	public String getWebClassesPath() {
		String path = getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		return path;
	}
	
	
	/**
	 * 获取WebInfPath
	 * 
	 * @param
	 * 
	 * @return String
	 */
	public String getWebInfPath() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF") + 8);
		} else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}

	/**
	 * 获取WebRoot
	 * 
	 * @param
	 * 
	 * @return String
	 */
	public String getWebRoot() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF/classes"));
		} else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}
}

package util.helper;

import com.infofuse.business.admin.dictionary.enums.DictionaryType;
import com.infofuse.business.admin.dictionary.po.DictionaryEntity;
import com.infofuse.business.admin.dictionary.service.DictionaryService;
import com.infofuse.redis.service.MyRediService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis中获取字典数据，供页面使用
 * @author liujiegang
 */
@Service("dictionaryHelper")
public class DictionaryHelper {

    public static final String REDIS_KEY_DIC = "dictionary";

    @Resource
    private MyRediService myRediService;
    @Resource
    private DictionaryService dictionaryService;

    /**
     * 服务启动时，初始化字典数据到Redis
     */
    public void initDicToRedis() {
        this.setDicToRedis(dictionaryService.findDictionaries());
    }

    /**
     * 初始化字典数据到Redis
     */
    private void setDicToRedis(List<DictionaryEntity> dicList) {
        for (DictionaryEntity d : dicList) {
            String key = REDIS_KEY_DIC + ":" + d.getTypeCode();
            if(StringUtils.isNotEmpty(d.getParentDictionaryCode()) && !"-1".equals(d.getParentDictionaryCode())) {
                key += ":" + d.getParentDictionaryCode();
            }
            String value = d.getDictionaryCode() + ":" + d.getDictionaryName() + ":" + d.getSortNumber();
            myRediService.add(key, value, d.getSortNumber());
        }
    }

    /**
     * 从Redis获取字典数据根据KEY
     * @param dicType
     * @param parentCode
     * @return
     */
    public Map<String, String> getDicByKey(DictionaryType dicType, String parentCode) {
        Map<String, String> dicMap = new LinkedHashMap<>();
        String redisKey = REDIS_KEY_DIC + ":" + dicType.getCode();
        if(parentCode != null) {
            redisKey += ":" + parentCode;
        }
        Set<String> dicSet = myRediService.getAllSetByKey(redisKey);
        if(dicSet != null && dicSet.size() > 0) {
            for (String dic : dicSet) {
                String[] dicArray = dic.split(":");
                dicMap.put(dicArray[0], dicArray[1]);
            }
        }
        return dicMap;
    }

    /**
     * 根据code获取字典名称
     * @param dicType
     * @param parentCode
     * @return
     */
    public Map<String, String> getDicByKey(String dicType, String parentCode) {
        Map<String, String> dicMap = new LinkedHashMap<>();
        String redisKey = REDIS_KEY_DIC + ":" + dicType;
        if(parentCode != null) {
            redisKey += ":" + parentCode;
        }
        Set<String> dicSet = myRediService.getAllSetByKey(redisKey);
        if(dicSet != null && dicSet.size() > 0) {
            for (String dic : dicSet) {
                String[] dicArray = dic.split(":");
                dicMap.put(dicArray[0], dicArray[1]);
            }
        }
        return dicMap;
    }


    /**
     * 刷新指定类型的字典数据
     * @param dictionaries
     * @param flushKey
     */
    public void flushDictionariesByType(List<DictionaryEntity> dictionaries, String flushKey) {
        myRediService.deleteByKey(REDIS_KEY_DIC + ":" + flushKey);
        this.setDicToRedis(dictionaries);
    }
}

/**
 * JedisUtils
 */
package Redis.utils;
import java.io.InputStream;
import java.util.Properties;
/**
 * ��ȡָ��������������ֵ 
 * @author ZL
 * @version 1.0
 */
public class pageuntil {
    private static pageuntil getPropValueIstance; //�����Լ�ʵ��
    private static Properties properties = new Properties();
    private static InputStream is;

    /**
     * ��ȡ�������(����ģʽ) 
     * @return �������
     */
    public static pageuntil getInstance(){
        if(getPropValueIstance == null){
            getPropValueIstance = new pageuntil();
        }
        return getPropValueIstance;
    }

    /**
     * ����properties�ļ������Ե�ֵ 
     * @param path properties�ļ�·�� 
     * @param PropertyName properties�ļ��е������� 
     * @return properties�ļ������Ե�ֵ
     */
    public static String getValue(String path, String PropertyName){
        try {
            is = pageuntil.class.getClassLoader().getResourceAsStream(path);
            properties.load(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("�쳣:·�����ļ�������!·��ǰ�벻Ҫ��\"/\"");
        }
        String propertyValue = (String) properties.get(PropertyName);
        return propertyValue;
    }

}  
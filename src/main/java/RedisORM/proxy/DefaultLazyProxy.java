package RedisORM.proxy;

import RedisORM.Configuration;
import RedisORM.executor.Execute;
import RedisORM.executor.ItemBuilderAssist;
import RedisORM.executor.opItem.HashItem;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultLazyProxy implements LazyProxy {

    private Configuration configuration;
    private Object target;
    private HashItem hashItem;
    private String id;
    private String key;
    private Set<String> properties ;
    private Set<String> change;


    public DefaultLazyProxy(Configuration configuration, Set<String> change, Object target, HashItem hashItem, String id) {
        this.configuration = configuration;
        this.change = change;
        this.target = target;
        this.hashItem = hashItem;
        this.id = id;
        this.key = target.getClass().getCanonicalName()+"$"+id;

        Object realValue = ItemBuilderAssist.ChangeType(hashItem.getIdType(),id);
        try {
            hashItem.getIdSetMethod().invoke(target,realValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        properties = hashItem.getExecutes().keySet();
    }

    public Object getProxyInstance(){
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return en.create();
    }


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy)  {
        Object returnValue = null;
        String methodName = method.getName();

        if(properties.size()!=0) {
            String property = methodName.substring(3, methodName.length()).toLowerCase();
            if (methodName.startsWith("get")) {
                if (properties.contains(property)) {
                    LoadProperty(property);
                }
            }
            if (methodName.equals("toString")) {
                CompleteLoad();
            }
            if(methodName.startsWith("set")){
                properties.remove(property);
                change.add(property);
            }
        }
        try {
            returnValue = method.invoke(target, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private void CompleteLoad() {

        Map<String,Execute> executeMap = hashItem.getExecutes();
        for(String s:properties){
            Execute exe = executeMap.get(s);
            if(exe.getClass()==HashItem.class){
                LoadHash(exe,s);
            }else{
                Jedis jedis = configuration.getDataSource().getJedis();
                exe.get(jedis,key,target);
                jedis.close();
            }
        }
        properties.clear();
    }

    private void LoadProperty(String property){
        Execute execute = hashItem.getExecutes().get(property);
        if(execute!=null){
            if(execute.getClass()==HashItem.class){
                LoadHash(execute,property);
            }else{
                Jedis jedis = configuration.getDataSource().getJedis();
                execute.get(jedis,key,target);
                jedis.close();
            }
        }
        properties.remove(property);
    }

    // 加载嵌套类
    private void LoadHash(Execute execute,String property){


        Jedis jedis = configuration.getDataSource().getJedis();
        String[] subids = jedis.hget(key,"#{subIds}").split("\\|");
        List<String> ids = hashItem.getHashproperty();
        int index=-1;
        // 逐个比较property，直到找到想要的那个的下标
        for(int i = 0;i<ids.size();i++){
            if(ids.get(i).equals(property)){
                index=i;
                break;
            }
        }
        String subid = null;
        if(index!=-1) subid = subids[index];
        Object value =execute.get(jedis,key,subid);
        jedis.close();
        try {
//            System.out.println("completeLoad "+property+" "+index+" "+value);
            hashItem.getSetHashField().get(index).invoke(target,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

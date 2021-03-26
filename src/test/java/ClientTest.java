import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class ClientTest {

    static final String answer = "{\"reply\":\"The factorial of 100 is: "+
            "933262154439441526816992388562667004907159682643816214685929638952175999932299156089414639761565182" +
            "86253697920827223758251185210916864000000000000000000000000\"}";
    @Test
    void multiClientTest() throws Exception{

        final int N = 100;
        final ExecutorService pool = Executors.newFixedThreadPool(N);

        List<String> list = new ArrayList<>();
        for(int i = 0;i<N;i++){
            String requestData =String.format("{\"username\" : \"user%d\", \"password\" : \"%d\"}",i,i);
            Future<String> future = pool.submit(() ->{
                Client client=new Client();
                client.sendPostRegister(requestData).statusCode();
                return client.sendPostLogin(requestData).body();

            });
            list.add(future.get());
        }
        List<String> corrList = new ArrayList<>();

        for(int i = 0;i<N;i++)
            corrList.add(answer);
        assertEquals(corrList,list);
    }
}
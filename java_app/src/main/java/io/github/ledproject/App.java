package io.github.ledproject;

import org.atsign.client.api.AtClient;
import org.atsign.common.AtSign;
import org.atsign.common.KeyBuilders;
import org.atsign.common.Keys.PublicKey;

public class App
{
    public static void main(String[] args) throws Exception
    {
        AtSign atSign = new AtSign("@computer0");
        AtSign pico = new AtSign("@maximumcomputer");
        AtClient atClient = AtClient.withRemoteSecondary(atSign);
        PublicKey pk = new KeyBuilders.PublicKeyBuilder(pico).key("led").build(); // public:led@computer0
        String previousValue = null;
        
        while (true)
        {
            Thread.sleep(500);
            
            String key = "led";
            atClient.executeCommand("delete:cached:public:" + key + pico, false);
            
            String data = atClient.get(pk).get();
            
            if (!data.equals(previousValue))
            {
                System.out.println("We got a new value! Value: " + data);
                previousValue = data;
            }
        }
    }
}

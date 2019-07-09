package io.th0rgal.bbissue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class Core {

    public static void main(String[] args) throws ClassNotFoundException {

        final String nms = "net.minecraft.server.v1_12_R1.";

        Class<?> serverPingClass = Class.forName(nms + "ServerPing");

        JsonElement exampleResponse = new JsonParser().parse("{\"description\":{\"text\":\"A Hacked Server\"}," +
                "\"players\":{\"max\":20,\"online\":0},\"version\":{\"name\":\"Paper 1.12.2\",\"protocol\":340}}");

        Class<?> serverPingSerializerClone = new ByteBuddy()
                .redefine(Class.forName(nms + "ServerPing$Serializer"))
                .method(ElementMatchers.named("a")
                        .and(ElementMatchers.returns(JsonElement.class)))
                .intercept(FixedValue.value(exampleResponse))
                .make()
                .load(Core.class.getClassLoader()).getLoaded();

        try {
            System.out.println("customized clone:" + serverPingSerializerClone.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}

package eti.italiviocorrea.services;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CfopService {

//    public Uni<String> greeting(String name) {
//        return Uni.createFrom().item(name)
//                .onItem().apply(n -> String.format("hello %s", name));
//    }

    public Uni<String> greeting(String name) {
        System.out.println("grreeting");
        return Uni.createFrom().item(formatString(name))
                .subscribeOn(Infrastructure.getDefaultExecutor());
    }

    private String formatString(String name) {
        System.out.println("teste");
        return String.format("hello %s", name);
    }

}

package eti.italiviocorrea.tracingdemo.adapters.inbound.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EShopController {

    @RequestMapping("/checkout")
    public String checkout() {
        return "Você verificou seu carrinho de compras com sucesso";
    }
}

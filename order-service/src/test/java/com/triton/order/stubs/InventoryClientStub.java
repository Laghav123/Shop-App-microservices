package com.triton.order.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

// A stub is wiremock thing which is used for mocking a network call
public class InventoryClientStub {

    // following stub mocks that inventory is in stock
    public static void stubInventoryCall(String skuCode, Integer quantity) {
        stubFor(
                get(
                        urlEqualTo("/api/inventory?skuCode=" + skuCode + "&quantity=" + quantity)
                ).willReturn(
                    aResponse()
                    .withStatus (200)
                    .withHeader("Content-Type", "application/json")
                    .withBody ("true")
                )
        );
    }


    // following stub mocks that inventory is NOT in stock
    public static void stubInventoryCallFail(String skuCode, Integer quantity) {
        stubFor(
                get(
                        urlEqualTo("/api/inventory?skuCode=" + skuCode + "&quantity=" + quantity)
                ).willReturn(
                        aResponse()
                                .withStatus (200)
                                .withHeader("Content-Type", "application/json")
                                .withBody ("false")
                )
        );
    }

}

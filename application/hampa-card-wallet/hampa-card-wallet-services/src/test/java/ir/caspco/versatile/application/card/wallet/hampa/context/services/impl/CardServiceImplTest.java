package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.configuration.CardWalletConfiguration;
import ir.caspco.versatile.common.util.encryption.Cryptography;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CardWalletConfiguration.class
})
@TestPropertySource("classpath:service-test.properties")
class CardServiceImplTest {

    @Autowired
    @Qualifier("caspian-rsa")
    private Cryptography caspianRsa;

    @Autowired
    @Qualifier("hampa-rsa")
    private Cryptography hampaRsa;

    @Test
    void cardInfo() {

        String jsonEncrypted = "KfZ00rIJOW64vLcWZnuJhgjPFl/bvnE7rzrZyY+VZTedf7gGprNpxZXjDAGiFa5oJTbu3hJaf+Sc\r\n+zCkxd/ReVTu5+FF0ODng36gPkc9hG0MV5eDrnG43GPtQYbWTicKwVJk9g1mdP/SBjBMV355z89r\r\nTo52vWacp/30wfh83McFey7rl7DQO40BC/Jfuot/NBJcyZAzFdVi+14VrXjzA8V46i66qUg6p3p5\r\nC5MOzsQDFJksN+3UDkFDXpK434tzrqqFkMeh0tIvomTgNiYD2pgMVxa6OXzJJQozrMJyUTQIy+gb\r\ncRfZ6kmKYSFBpEUGENts4dpDftJXDvNr6sB4gA==";
        String jsonSignature = "rLMdDbKde9Fc9qoenOpfrp/LS+aMtqm20hWd5Jye6HPl/ofiHPJkH7hzt/nHNasFwUQS8EbdqtTG\r\n031oBauI3ZvdV4AelZzvut83Kzh5++3bTGmwfpFui3PeBIComMXVuyOmtCCSIXBPxNrprJ8w+QpR\r\nYxh4KvZQ7gzX+sKkdBeZzZPyQZExzG+1f76tRICtug3QcECugtEGZHsxnQahUSlQs6aAWTMgHQAl\r\nSa6em/DkZg9SmjCS/39XMoMQ88uCnd118rhKut4wrbzorAERzehYr68+hTYJLxg55LvipG0W2YTK\r\nUQKjyguyE+SdxLivIjMFiT2eKXy+KIdmCVoArg==";

        String json = hampaRsa.decrypt(jsonEncrypted);
        assertTrue(caspianRsa.verify(jsonSignature, json));
    }
}
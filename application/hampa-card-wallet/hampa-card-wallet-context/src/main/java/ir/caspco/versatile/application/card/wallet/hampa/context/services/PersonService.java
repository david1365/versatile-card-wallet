package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface PersonService {

    PersonEntity findByNationalCode(String nationalCode);

    PersonEntity findByMobileNumber(String mobileNumber);
}

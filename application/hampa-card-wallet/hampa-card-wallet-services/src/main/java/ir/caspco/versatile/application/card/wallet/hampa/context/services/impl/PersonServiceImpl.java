package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.PersonEntity;
import ir.caspco.versatile.application.card.wallet.hampa.context.exceptions.PersonNotFoundException;
import ir.caspco.versatile.application.card.wallet.hampa.context.repositories.PersonRepository;
import ir.caspco.versatile.application.card.wallet.hampa.context.services.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */


//TODO from davood akbari: Do not forget to test.

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;


    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public PersonEntity findByNationalCode(String nationalCode) {

        return personRepository.findByNationalCode(nationalCode)
                .orElseThrow(PersonNotFoundException::new);
    }

    @Override
    public PersonEntity findByMobileNumber(String mobileNumber) {

        return personRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(PersonNotFoundException::new);
    }
}

package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, UUID>, CrudRepository<MerchantEntity, UUID> {

    Optional<MerchantEntity> findByMerchantIdAndActiveTrue(UUID merchantId);
}

package ir.caspco.versatile.application.card.wallet.hampa.context.repositories;

import ir.caspco.versatile.application.card.wallet.hampa.context.domains.ReverseTranLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ReverseTranLogRepository extends JpaRepository<ReverseTranLogEntity, BigDecimal> {
}

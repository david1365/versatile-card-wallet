package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.MerchantService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.MerchantVO;
import ir.caspco.versatile.common.validation.annotations.IsValidUUID;
import ir.caspco.versatile.context.validation.groups.DInsert;
import ir.caspco.versatile.context.validation.groups.DUpdate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}/merchant")
public class MerchantController {

    private final MerchantService merchantService;


    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }


    @PreAuthorize("hasRole('ROLE_CW_SAVE_MERCHANT')")
    @PostMapping
    public Mono<MerchantVO> save(@RequestBody @Validated(DInsert.class) @Valid MerchantVO merchant) {

        return Mono.just(merchantService.save(merchant));
    }

    @PreAuthorize("hasRole('ROLE_CW_UPDATE_MERCHANT')")
    @PatchMapping
    public Mono<MerchantVO> update(@RequestBody @Validated(DUpdate.class) @Valid MerchantVO merchant) {

        return Mono.just(merchantService.update(merchant));
    }

    @PreAuthorize("hasRole('ROLE_CW_FIND_MERCHANT')")
    @GetMapping("/{merchantId}")
    public Mono<MerchantVO> find(@NotEmpty @NotBlank @NotEmpty @IsValidUUID @PathVariable UUID merchantId) {
        return Mono.just(merchantService.findById(merchantId));
    }

    @PreAuthorize("hasRole('ROLE_CW_DELETE_MERCHANT')")
    @DeleteMapping("/{merchantId}")
    public Mono<Boolean> delete(@NotEmpty @NotBlank @NotEmpty @IsValidUUID @PathVariable UUID merchantId) {

        return Mono.just(merchantService.deleteById(merchantId));
    }
}

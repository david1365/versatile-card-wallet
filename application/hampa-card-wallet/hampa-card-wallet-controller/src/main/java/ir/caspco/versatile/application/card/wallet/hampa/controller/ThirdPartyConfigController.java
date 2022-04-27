package ir.caspco.versatile.application.card.wallet.hampa.controller;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.ThirdPartyConfigService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InternetPackageResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InternetPackageSearchEntranceVO;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ThirdPartyGroupType;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyConfigVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartySubGroupVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${cardWallet.prefix}")
public class ThirdPartyConfigController {

    private final ThirdPartyConfigService thirdPartyConfigService;


    public ThirdPartyConfigController(ThirdPartyConfigService thirdPartyConfigService) {

        this.thirdPartyConfigService = thirdPartyConfigService;
    }


    @PreAuthorize("hasRole('ROLE_CW_LOAD_BY_THIRD_PARTY_GROUP_TYPE')")
    @GetMapping("/loadByThirdPartyGroupType/{thirdPartyGroupType}")
    public Mono<List<ThirdPartySubGroupVO>> cardBalance(
            @NotEmpty @NotBlank @NotEmpty @PathVariable ThirdPartyGroupType thirdPartyGroupType) {

        return Mono.just(thirdPartyConfigService.loadByThirdPartyGroupType(thirdPartyGroupType));

    }

    @PreAuthorize("hasRole('ROLE_CW_LOAD_THIRD_PARTY_CONFIGS_BY_PARENT_ID')")
    @GetMapping("/loadThirdPartyConfigsByParentId/{parentId}")
    public Mono<List<ThirdPartyConfigVO>> loadThirdPartyConfigsByParentId(
            @NotEmpty @NotBlank @NotEmpty @PathVariable Long parentId) {

        return Mono.just(thirdPartyConfigService.loadThirdPartyConfigsByParentId(parentId));
    }

    @PreAuthorize("hasRole('ROLE_CW_SEARCH_INTERNET_PACKAGES')")
    @PostMapping("/searchInternetPackages")
    public Mono<List<InternetPackageResultVO>> searchInternetPackages(@Valid @RequestBody InternetPackageSearchEntranceVO internetPackageSearch) {

        return Mono.just(thirdPartyConfigService.searchInternetPackages(internetPackageSearch));
    }
}

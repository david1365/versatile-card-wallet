package ir.caspco.versatile.application.card.wallet.hampa.context.services.impl;

import ir.caspco.versatile.application.card.wallet.hampa.context.services.ThirdPartyConfigService;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InternetPackageResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InternetPackageSearchEntranceVO;
import ir.caspco.versatile.jms.client.common.client.LoadThirdPartyConfigsByParentIdClient;
import ir.caspco.versatile.jms.client.common.client.LoadThirdPartySubGroupByThirdPartyGroupTypeClient;
import ir.caspco.versatile.jms.client.common.client.SearchInternetPackagesClient;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.PackageDurationCredit;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ThirdPartyGroupType;
import ir.caspco.versatile.jms.client.common.msg.LoadThirdPartyConfigsRequest;
import ir.caspco.versatile.jms.client.common.msg.LoadThirdPartyConfigsResponse;
import ir.caspco.versatile.jms.client.common.msg.SearchInternetPackagesRequest;
import ir.caspco.versatile.jms.client.common.msg.SearchInternetPackagesResponse;
import ir.caspco.versatile.jms.client.common.vo.InternetPackageOperatorVO;
import ir.caspco.versatile.jms.client.common.vo.InternetPackageSearchVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyConfigVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartySubGroupVO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThirdPartyConfigServiceImpl implements ThirdPartyConfigService {

    private final LoadThirdPartySubGroupByThirdPartyGroupTypeClient loadThirdPartySubGroupByThirdPartyGroupTypeClient;
    private final LoadThirdPartyConfigsByParentIdClient loadThirdPartyConfigsByParentIdClient;
    private final SearchInternetPackagesClient searchInternetPackagesClient;

    public ThirdPartyConfigServiceImpl(
            LoadThirdPartySubGroupByThirdPartyGroupTypeClient loadThirdPartySubGroupByThirdPartyGroupTypeClient,
            LoadThirdPartyConfigsByParentIdClient loadThirdPartyConfigsByParentIdClient,
            SearchInternetPackagesClient searchInternetPackagesClient) {

        this.loadThirdPartySubGroupByThirdPartyGroupTypeClient = loadThirdPartySubGroupByThirdPartyGroupTypeClient;
        this.loadThirdPartyConfigsByParentIdClient = loadThirdPartyConfigsByParentIdClient;
        this.searchInternetPackagesClient = searchInternetPackagesClient;
    }

    @Override
    public List<ThirdPartyConfigVO> loadThirdPartyConfigsByParentId(Long parentId) {
        LoadThirdPartyConfigsRequest loadThirdPartyConfigsRequest = LoadThirdPartyConfigsRequest.builder()
                .parentId(parentId)
                .build();

        LoadThirdPartyConfigsResponse loadThirdPartyConfigsResponse =
                loadThirdPartyConfigsByParentIdClient.send(loadThirdPartyConfigsRequest)
                        .retrieve()
                        .result()
                        .orElse(
                                LoadThirdPartyConfigsResponse.builder()
                                        .thirdPartyConfigs(Collections.emptyList())
                                        .build()
                        );

        return loadThirdPartyConfigsResponse.getThirdPartyConfigs();
    }

    @Override
    public List<ThirdPartySubGroupVO> loadByThirdPartyGroupType(ThirdPartyGroupType thirdPartyGroupType) {

        return loadThirdPartySubGroupByThirdPartyGroupTypeClient.loadByThirdPartyGroupType(thirdPartyGroupType);
    }

    @Override
    public List<InternetPackageResultVO> searchInternetPackages(InternetPackageSearchEntranceVO internetPackageSearch) {

        final PackageDurationCredit durationCredit = internetPackageSearch.getDurationCredit();

        SearchInternetPackagesRequest searchInternetPackagesRequest = SearchInternetPackagesRequest.builder()
                .internetPackageSearch(
                        InternetPackageSearchVO.builder()
                                .operator(internetPackageSearch.getOperator())
                                .packageType(internetPackageSearch.getPackageType())
                                .durationCredit(durationCredit == null ? null : internetPackageSearch.getDurationCredit().value())
                                .fromAmount(internetPackageSearch.getFromAmount())
                                .toAmount(internetPackageSearch.getToAmount())
                                .build()
                )
                .build();

        SearchInternetPackagesResponse searchInternetPackagesResponse = searchInternetPackagesClient.send(searchInternetPackagesRequest)
                .retrieve()
                .result()
                .orElse(
                        SearchInternetPackagesResponse.builder()
                                .internetListInfos(Collections.emptyList())
                                .build()
                );

        return searchInternetPackagesResponse.getInternetListInfos().stream()
                .map(internetPackage ->
                        {
                            InternetPackageOperatorVO operator = internetPackage.getOperator();

                            return InternetPackageResultVO.builder()
                                    .code(internetPackage.getCode())
                                    .name(internetPackage.getName())
                                    .operator(operator == null ? null : internetPackage.getOperator().getOperator())
                                    .simType(internetPackage.getSimType())
                                    .amount(internetPackage.getAmount())
                                    .isActive(internetPackage.getIsActive())
                                    .durationHours(internetPackage.getDurationHours())
                                    .build();
                        }
                ).collect(Collectors.toList());
    }
}

package com.gzu.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class GatewaySentinelConfig {

    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(routeRule("auth-service"));
        rules.add(routeRule("user-service"));
        rules.add(routeRule("stock-service"));
        rules.add(routeRule("order-service"));
        rules.add(routeRule("message-service"));
        GatewayRuleManager.loadRules(rules);
    }

    @PostConstruct
    public void initGatewayApiDefinitions() {
        Set<ApiDefinition> definitions = new HashSet<>();
        definitions.add(apiDefinition("api-auth", "/api/auth/**"));
        definitions.add(apiDefinition("api-users", "/api/users/**"));
        definitions.add(apiDefinition("api-stock", "/api/stock/**"));
        definitions.add(apiDefinition("api-orders", "/api/orders/**"));
        definitions.add(apiDefinition("api-messages", "/api/messages/**"));
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private GatewayFlowRule routeRule(String routeId) {
        return new GatewayFlowRule(routeId)
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setCount(1000)
                .setIntervalSec(1);
    }

    private ApiDefinition apiDefinition(String apiName, String pattern) {
        Set<ApiPredicateItem> items = new HashSet<>();
        items.add(new ApiPathPredicateItem()
                .setPattern(pattern)
                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
        return new ApiDefinition(apiName).setPredicateItems(items);
    }
}

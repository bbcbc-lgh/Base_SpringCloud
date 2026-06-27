package com.gzu.common.core.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SentinelRuleConfig {

    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        FlowRule rule = new FlowRule();
        rule.setResource("order-service");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(1000);
        rule.setLimitApp("default");
        rules.add(rule);

        FlowRuleManager.loadRules(rules);
    }

    @PostConstruct
    public void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();

        DegradeRule rule = new DegradeRule();
        rule.setResource("stock-service");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setCount(1000);
        rule.setTimeWindow(10);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
    }
}

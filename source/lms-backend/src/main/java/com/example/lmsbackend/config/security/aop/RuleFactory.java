package com.example.lmsbackend.config.security.aop;

import com.example.lmsbackend.config.security.aop.rule.Rule;
import com.example.lmsbackend.enums.ResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RuleFactory {
    private final List<Rule> rules;

    private final Map<ResourceType, Rule> ruleMap = new EnumMap<>(ResourceType.class);

    @PostConstruct
    public void initRuleMap(){
        for (Rule rule: rules){
            ruleMap.put(rule.getResourceType(),rule);
        }
    }

    public Rule getRule(ResourceType type) {
        return ruleMap.get(type);
    }
}

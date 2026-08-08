package io.github.aniruddhamaity911.activecache.key;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Evaluates Spring Expression Language (SpEL) expressions against
 * the arguments of an intercepted method.
 *
 * <p>This evaluator is primarily used by the caching aspects to resolve
 * dynamic cache keys defined in annotations.</p>
 *
 * <p>For example:</p>
 *
 * <pre>
 * {@code
 * @CacheRead(
 *     cacheName = "users",
 *     key = "#userId"
 * )
 * public User getUser(Long userId) {
 *     ...
 * }
 * }
 * </pre>
 *
 * <p>If {@code userId} is {@code 101}, evaluating {@code #userId}
 * returns {@code 101}.</p>
 */
@Component
public class SpelKeyEvaluator {

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ParameterNameDiscoverer parameterNameDiscoverer =
            new DefaultParameterNameDiscoverer();

    /**
     * Evaluates a SpEL expression using the intercepted method's
     * parameters as the evaluation context.
     *
     * @param expression SpEL expression used to generate the cache key,
     *                   for example {@code "#userId"}
     * @param method the intercepted method
     * @param arguments arguments passed to the intercepted method
     * @param target target object on which the method is invoked
     * @return the value produced by evaluating the SpEL expression
     */
    public Object evaluate(
            String expression,
            Method method,
            Object[] arguments,
            Object target
    ) {
        EvaluationContext context = new MethodBasedEvaluationContext(
                target,
                method,
                arguments,
                parameterNameDiscoverer
        );

        return parser.parseExpression(expression).getValue(context);
    }
}

package calls;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import model.CalculationResult;
import model.SimpleCalculationRequest;

public class CalcSumServiceCall extends AbstractServiceDiscoveryCommand<SimpleCalculationRequest, CalculationResult> {

    Cache<Pair<Integer, Integer>, CalculationResult> cache;

    public CalcSumServiceCall(Optional<Pair<String, String>> serviceDiscoveryCredentials,
                              SimpleCalculationRequest request) {
        super("wellnr-calc-sum-service", serviceDiscoveryCredentials, "/api/calculation/add", request,
                CalculationResult.class);

        cache = //
                CacheBuilder.newBuilder() //
                        .maximumSize(100) //
                        .expireAfterWrite(10, TimeUnit.MINUTES) //
                        .build();
    }

    @Override
    protected CalculationResult run() throws Exception {
        CalculationResult result = super.run();
        cache.put(Pair.of(Integer.valueOf(this.request.n1), Integer.valueOf(this.request.n2)), result);
        return result;
    }

    @Override
    protected CalculationResult getFallback() {
        CalculationResult result = cache
                .getIfPresent(Pair.of(Integer.valueOf(this.request.n1), Integer.valueOf(this.request.n2)));

        if (result == null) {
            // Might not be the best solution here ...
            throw new RuntimeException("Unable to create fallback");
        }

        return result;
    }

}

package com.order.service.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;

public class CorrelationIdProducerInterceptor implements ProducerInterceptor<String, String> {
	
	 private static final String CORRELATION_ID_KEY = "correlation-id";
	    private static final String MDC_TRACE_ID_KEY = "traceId";

	    @Override
	    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
	        String correlationId = MDC.get(MDC_TRACE_ID_KEY);
	        if (correlationId == null) {
	            correlationId = UUID.randomUUID().toString();
	            MDC.put(MDC_TRACE_ID_KEY, correlationId);
	        }
	        // Add correlation-id header
	        record.headers().add(CORRELATION_ID_KEY, correlationId.getBytes(StandardCharsets.UTF_8));
	        return record;
	    }

	    @Override
	    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
	        // Clear MDC after send acknowledgement to avoid memory leaks
	        MDC.remove(MDC_TRACE_ID_KEY);
	    }

	    @Override
	    public void close() {
	        MDC.remove(MDC_TRACE_ID_KEY);
	    }

	    @Override
	    public void configure(Map<String, ?> configs) {
	        // no-op
	    }

}

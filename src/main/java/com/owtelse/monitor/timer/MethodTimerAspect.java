package com.owtelse.monitor.timer;

import java.util.logging.Level;
import java.util.logging.Logger;

// TODO maybe swap from JAMon to this ? import javax.management.monitor.Monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@Aspect
public class MethodTimerAspect {
	private final static Logger LOG = Logger.getLogger(MethodTimerAspect.class
			.getName());

	@Pointcut("execution(public * *(..))")
	public void anyPublicMethod() {
	}

	@Around("anyPublicMethod() && @annotation(methodTimer")
	public Object methodTimerAroundAdvice(ProceedingJoinPoint joinPoint,
			MethodTimer methodTimer) {
		try {
			if (methodTimer.activeIf().isEmpty()) {
				return doAdvise(joinPoint, methodTimer);
			} else {
				return joinPoint.proceed();
			}
		} catch (Throwable t) {
			LOG.setLevel(Level.WARNING);
			LOG.warning("Error applying around advice at :- "
					+ joinPoint.getSignature().toLongString() + " ::- "
					+ joinPoint.getSourceLocation().getFileName() + ":"
					+ joinPoint.getSourceLocation().getLine());
			return null;
		}

	}

	Monitor start(String label) {
		return MonitorFactory.start(label);
	}

	/**
	 * @param joinPoint
	 * @param timer
	 * @return
	 */
	String generateLabel(ProceedingJoinPoint joinPoint, MethodTimer timer) {
		StringBuilder label = new StringBuilder(255);
		// do root
		if (timer.value().isEmpty()) {
			label.append(joinPoint.getSignature().toLongString());
		} else {
			label.append(timer.value());
		}

		// doSuffix
		if (!timer.suffix().isEmpty()) {
			label.append(timer.suffix());
		}
		label.trimToSize();
		return label.toString();

	}

	Object doAdvise(ProceedingJoinPoint joinPoint, MethodTimer methodTimer) throws Throwable {
		Monitor m = start(generateLabel(joinPoint,methodTimer));
		Object continuation = joinPoint.proceed();
		m.stop();
		return continuation;
	}

}

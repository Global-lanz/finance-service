package lanz.global.financeservice.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public enum FrequencyEnum {

    ONLY_ONCE {
        @Override
        public int calculateInstallmentQuantity(LocalDate start, LocalDate end) {
            return 1;
        }

        @Override
        public LocalDate calculateStartDateReference(LocalDate contractStart, Integer paymentDay, DayOfWeek weekPaymentDay) {
            LocalDate startDateReference = contractStart.withDayOfMonth(paymentDay);
            while (startDateReference.isBefore(contractStart)) {
                startDateReference = startDateReference.plusMonths(1);
            }
            return startDateReference;
        }

        @Override
        public LocalDate calculateDueDate(LocalDate startDateReference, int invoiceNumber) {
            return startDateReference;
        }
    }, WEEKLY {
        @Override
        public int calculateInstallmentQuantity(LocalDate start, LocalDate end) {
            return Math.max((int) ChronoUnit.DAYS.between(start, end) / 7, 1);
        }

        @Override
        public LocalDate calculateStartDateReference(LocalDate contractStart, Integer paymentDay, DayOfWeek weekPaymentDay) {
            return contractStart.with(weekPaymentDay);
        }

        @Override
        public LocalDate calculateDueDate(LocalDate startDateReference, int invoiceNumber) {
            return startDateReference.plusWeeks(invoiceNumber);
        }
    }, MONTHLY {
        @Override
        public int calculateInstallmentQuantity(LocalDate start, LocalDate end) {
            return Math.max(Period.between(start, end).getMonths(), 1);
        }

        @Override
        public LocalDate calculateStartDateReference(LocalDate contractStart, Integer paymentDay, DayOfWeek weekPaymentDay) {
            LocalDate startDateReference = contractStart.withDayOfMonth(paymentDay);
            while (startDateReference.isBefore(contractStart)) {
                startDateReference = startDateReference.plusMonths(1);
            }
            return startDateReference;
        }

        @Override
        public LocalDate calculateDueDate(LocalDate startDateReference, int invoiceNumber) {
            return startDateReference.plusMonths(invoiceNumber);
        }
    }, ANNUALLY {
        @Override
        public int calculateInstallmentQuantity(LocalDate start, LocalDate end) {
            return Math.max(Period.between(start, end).getYears(), 1);
        }

        @Override
        public LocalDate calculateStartDateReference(LocalDate contractStart, Integer paymentDay, DayOfWeek weekPaymentDay) {
            LocalDate startDateReference = contractStart.withDayOfMonth(paymentDay);
            while (startDateReference.isBefore(contractStart)) {
                startDateReference = startDateReference.plusMonths(1);
            }
            return startDateReference;
        }

        @Override
        public LocalDate calculateDueDate(LocalDate startDateReference, int years) {
            return startDateReference.plusYears(years);
        }
    };

    public abstract int calculateInstallmentQuantity(LocalDate start, LocalDate end);

    public abstract LocalDate calculateStartDateReference(LocalDate contractStart, Integer paymentDay, DayOfWeek weekPaymentDay);

    public abstract LocalDate calculateDueDate(LocalDate startDateReference, int invoiceNumber);
}

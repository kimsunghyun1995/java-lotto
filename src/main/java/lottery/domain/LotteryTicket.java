package lottery.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LotteryTicket {

    public static final int LOTTERY_NUMBER_COUNTS = 6;
    private final List<LotteryNumber> lotteryNumbers;

    private LotteryTicket(List<LotteryNumber> lotteryNumbers) {
        validateLotteryNumberCounts(lotteryNumbers);
        validateLotteryNumbersDuplication(lotteryNumbers);
        this.lotteryNumbers = lotteryNumbers;
    }

    public static LotteryTicket from(List<LotteryNumber> lotteryNumbers) {
        return new LotteryTicket(lotteryNumbers);
    }

    public static LotteryTicket from(String[] lotteryNumbers) {
        List<LotteryNumber> parsedLotteryNumbers = Arrays.stream(lotteryNumbers)
                .map(Integer::parseInt)
                .map(LotteryNumber::from)
                .collect(Collectors.toList());
        return new LotteryTicket(parsedLotteryNumbers);
    }

    public static LotteryTicket publishAutomaticLotteryTicket() {
        return new LotteryTicket(LotteryNumbersGenerator.generateAutomaticNumbers());
    }

    public List<Integer> getLotteryNumbers() {
        return lotteryNumbers.stream()
                .map(LotteryNumber::getLotteryNumber)
                .collect(Collectors.toList());
    }

    public LotteryRank getMatchLotteryRank(LotteryTicket lastWinnerTicket) {
        long matchNumberCounts = this.getLotteryNumbers().stream()
                .filter(target -> lastWinnerTicket.getLotteryNumbers().stream().anyMatch(Predicate.isEqual(target)))
                .count();
        return LotteryRank.valueOf((int) matchNumberCounts);
    }

    private void validateLotteryNumberCounts(List<LotteryNumber> lotteryNumbers) {
        if (lotteryNumbers.size() != LOTTERY_NUMBER_COUNTS) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_NUMBER_COUNTS);
        }
    }

    private void validateLotteryNumbersDuplication(List<LotteryNumber> lotteryNumbers) {
        long filteredLotteryNumberCounts = lotteryNumbers.stream()
                .map(LotteryNumber::getLotteryNumber)
                .distinct()
                .count();
        if (lotteryNumbers.size() != filteredLotteryNumberCounts) {
            throw new IllegalArgumentException(ErrorMessages.DUPLICATED_NUMBER);
        }
    }
}

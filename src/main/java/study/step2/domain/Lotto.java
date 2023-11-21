package study.step2.domain;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import study.step2.domain.exception.LottoException;

import static study.step2.domain.LottoGenerator.LOTTO_NUMBERS_SIZE;

public class Lotto {

    private final List<LottoNumber> lottoNumbers;
    private int hitCount = 0;

    public Lotto(List<Integer> numbers) {
        validateNumbers(numbers);
        this.lottoNumbers = numbers.stream()
            .map(LottoNumber::new)
            .collect(Collectors.toList());
    }

    public static void validateNumbers(List<Integer> numbers) {
        if (!isValidNumbers(numbers)) {
            throw new LottoException("번호는 6자리 중복되지 않은 값이어야 합니다.");
        }
    }

    private static boolean isValidNumbers(List<Integer> numbers) {
        return new HashSet<>(numbers)
            .size() == LOTTO_NUMBERS_SIZE;
    }

    public Rank matches(List<Integer> winningNumbers) {
        lottoNumbers.stream()
            .map(LottoNumber::lottoNumber)
            .forEach(number -> calculateHitCount(number, winningNumbers));
        return Rank.valueOfHitCount(hitCount);
    }

    private void calculateHitCount(Integer number, List<Integer> winningNumbers) {
        if (winningNumbers.contains(number)) {
            hitCount += 1;
        }
    }

    public List<LottoNumber> lottoNumbers() {
        return lottoNumbers;
    }

    public int hitCount() {
        return hitCount;
    }

}

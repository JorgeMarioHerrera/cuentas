package co.com.bancolombia.model.either;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"java:S118", "java:S1610", "java:S100", "java:S2293"})
public abstract class Either<L, R> {

    public static <L, R> Either<L, R> either(Supplier<L> leftSupplier, Supplier<R> rightSupplier) {
        R rightValue = rightSupplier.get();
        if (rightValue != null) {
            return Either.<L, R>right(rightValue);
        } else {
            return Either.<L, R>left(leftSupplier.get());
        }
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Left<L, R>(left);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Right<L, R>(right);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight);

    public abstract <T, U> Either<T, U> map(Function<R, U> transformRight);

    public abstract <T, U> Either<T, U> flatMap(Function<R, Either<T, U>> transformRight);

    public abstract void run(Consumer<L> runLeft, Consumer<R> runRight);

    public static class Left<L, R> extends Either<L, R> {

        protected L leftValue;

        private Left(L left) {
            this.leftValue = left;
        }

        @Override
        public L getLeft() {
            return this.leftValue;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("Tried to getRight from a Left");
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight) {
            return transformLeft.apply(this.leftValue);
        }

        @Override
        public <T, U> Either<T, U> map(Function<R, U> transformRight) {
            return (Either<T, U>) this;
        }

        @Override
        public <T, U> Either<T, U> flatMap(Function<R, Either<T, U>> transformRight) {
            return (Either<T, U>) this;
        }

        @Override
        public void run(Consumer<L> runLeft, Consumer<R> runRight) {
            runLeft.accept(this.leftValue);
        }


        @Override
        public int hashCode() {
            return this.leftValue.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Left<?, ?>) {
                final Left<?, ?> otherAsLeft = (Left<?, ?>) other;
                return this.leftValue.equals(otherAsLeft.leftValue);
            } else {
                return false;
            }
        }

    }

    public static class Right<L, R> extends Either<L, R> {

        protected R rightValue;

        private Right(R right) {
            this.rightValue = right;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("Tried to getLeft from a Right");
        }

        @Override
        public R getRight() {
            return rightValue;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight) {
            return transformRight.apply(this.rightValue);
        }

        @Override
        public <T, U> Either<T, U> map(Function<R, U> transformRight) {
            return Either.<T, U>right(transformRight.apply(this.rightValue));
        }

        @Override
        public <T, U> Either<T, U> flatMap(Function<R, Either<T, U>> transformRight) {
            return transformRight.apply(this.rightValue);
        }

        @Override
        public void run(Consumer<L> runLeft, Consumer<R> runRight) {
            runRight.accept(this.rightValue);
        }


        @Override
        public int hashCode() {
            return this.rightValue.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Right<?, ?>) {
                final Right<?, ?> otherAsRight = (Right<?, ?>) other;
                return this.rightValue.equals(otherAsRight.rightValue);
            } else {
                return false;
            }
        }

    }
}
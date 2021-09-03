object monadic {
    trait Monad[F[_]] {
        def flatMap[A, B](fa: F[A], afb: A => F[B]): F[B]
        def map[A, B](fa: F[A], ab: A => B): F[B]
    }

    object Monad {
        def apply[F[_]](implicit F: Monad[F]): Monad[F] = F
    }

    implicit class MonadImpl[F[_], A](fa: F[A]) {
        def map[B](f: A => B)(implicit F: Monad[F]): F[B] = F.map(fa, f)
        def flatMap[B](afb: A => F[B])(implicit F: Monad[F]): F[B] =
            F.flatMap(fa, afb)
    }
}

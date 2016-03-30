/*
 * @test /nodynamiccopyright/
 * @bug 6939620 7020044
 *
 * @summary  Check that diamond fails when inference violates declared bounds
 *           (test with inner class, qualified/simple type expressions)
 * @author mcimadamore
 * @compile/fail/ref=Neg03.out Neg03.java -XDrawDiagnostics
 *
 */

class Neg03<U> {

    class Foo<V extends Number> {
        Foo(V x) {}
        <Z> Foo(V x, Z z) {}
    }

    void testSimple() {
        Foo<String> f1 = new Foo<>("");
        Foo<? extends String> f2 = new Foo<>("");
        Foo<?> f3 = new Foo<>("");
        Foo<? super String> f4 = new Foo<>("");

        Foo<String> f5 = new Foo<>("", "");
        Foo<? extends String> f6 = new Foo<>("", "");
        Foo<?> f7 = new Foo<>("", "");
        Foo<? super String> f8 = new Foo<>("", "");
    }

}

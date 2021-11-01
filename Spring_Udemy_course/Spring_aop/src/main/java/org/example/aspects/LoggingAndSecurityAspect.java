package org.example.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.Book;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class LoggingAndSecurityAspect {

//    @Pointcut("execution(* org.example.UniLibrary.*())")
//    private void allGetMethodsFromUniLibrary(){}
//
//    @Pointcut("execution(public void org.example.UniLibrary.returnMagazine())")
//    private void returnMagazineFromUniLibrary(){}
//
//    @Pointcut("allGetMethodsFromUniLibrary() && !returnMagazineFromUniLibrary()")
//    private void allMethodExceptReturnMagazineFromUniLibrary(){}
//
//    @Before("allMethodExceptReturnMagazineFromUniLibrary()")
//    public void beforeAllMethodExceptReturnMagazineAdvice(){
//        System.out.println("beforeAllMethodExceptReturnMagazineAdvice: writing Log#9");
//    }



//    @Pointcut("execution(* org.example.UniLibrary.return*())")
//    private void allGetMethodsFromUniLibrary(){}
//
//    @Pointcut("execution(* org.example.UniLibrary.get*())")
//    private void allReturnMethodsFromUniLibrary(){}
//
//    @Pointcut("allGetMethodsFromUniLibrary() || allReturnMethodsFromUniLibrary()")
//    private void allGetAndReturnMethodsFromUniLibrary(){}
//
//
//    @Before("allGetMethodsFromUniLibrary()")
//    public void beforeGetLoggingAdvice(){
//        System.out.println("beforeGetLoggingAdvice: writing Log#1");
//    }
//
//    @Before("allReturnMethodsFromUniLibrary()")
//    public void beforeReturnLoggingAdvice(){
//        System.out.println("beforeGetLoggingAdvice: writing Log#2");
//    }
//
//    @Before("allGetAndReturnMethodsFromUniLibrary()")
//    public void beforeGetAndReturnLoggingAdvice(){
//        System.out.println("beforeGetAndReturnLoggingAdvice: writing Log#3");
//    }



    //@Before("execution(public void *(org.example.Book, ..))")
    //@Before("execution(public void *(*))") для одного параметра
    //@Before("execution(public void getBook())")
    //@Before("execution(public void org.example.UniLibrary.getBook())")
    @Before("org.example.aspects.MyPointcuts.allAddMethods()")
    public void beforeAddLoggingAdvice(JoinPoint joinPoint){

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        System.out.println("methodSignature = " +  methodSignature);
        System.out.println("methodSignature.getMethod() = " +  methodSignature.getMethod());
        System.out.println("methodSignature.getReturnType() = " +  methodSignature.getReturnType());
        System.out.println("methodSignature.getName() = " +  methodSignature.getName());

        if(methodSignature.getName().equals("addBook")){
            Object[] arguments = joinPoint.getArgs();
            for(Object obj:arguments){
                if(obj instanceof Book) {
                    Book myBook = (Book) obj;
                    System.out.println("Информация о книге: название - " +
                            myBook.getName() + ", автор - " + myBook.getAuthor() +
                            ", год издания - " + myBook.getYearOfPublication());
                }
                else if(obj instanceof String){
                    System.out.println("книгу добавиляет " + obj);
                }

            }
        }


        System.out.println("beforeGetBookAdvice: логирование попыткы получить книгу/журнал");
        System.out.println("--------------------------------------");
    }

//    @Before("execution(public * returnBook())")
    //@Before("execution(* returnBook())") без модификатора доступа и возврощаемого значения
    //@Before("execution(* *())") любой метод без параметров
//    public void beforeReturnBook(){
//        System.out.println("beforeReturnBook: попытка вернуть книгу");
//    }

//    @Before("allGetMethods()")
//    public void beforeGetSecurityAdvice(){
//        System.out.println("beforeGetSecurityAdvice: проверка прав на получение книги/журнала");
//    }
}

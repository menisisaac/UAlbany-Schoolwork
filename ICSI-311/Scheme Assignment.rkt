(define disk `("D" "top"
(
	("F" "file1.txt" 30)
	("D" "sub1"
	(
		( "F" "file1.txt" 1234)
		( "F" "file2.txt" 2345)
		( "F" "file3.txt" 3456)
	)
	)
	("D" "sub2"
	(
		( "F" "file1.txt" 1234)
		( "F" "file2.txt" 2345)
		( "F" "file3.txt" 3456)
	)
	)
)
)
)
(define file `( "F" "file2.txt" 2345))



(define (processElement a)
  (if (eq? (car a) "F")
      (cdr(cdr a)
))
  (if (eq? (car a) "D")
      (processDirectory(cdr(cdr a)))
  ))

(define (processDirectory a)
  
)

(define (sum a)
  (if (null? a)
      0
  (+ (processElement(car a)) (sum (cdr a)))
)
  


  


/**
 * Die Klassen in diesem Paket bringen die Funktionalität der Klassen aus dem Paket ‚process’ mit 
 * der Funktionalität der Klassen aus den Paketen ‚lp_problem’ und ‚simplex_problem’ zusammen. Denn
 * sie enthalten die eigentliche Implementation des Simplex-Algorithmus. Dabei nimmt die Klasse 
 * ProcessManager eine zentrale Rolle ein. Sie ist von der Klasse ProcessMaster abgeleitet und 
 * beinhaltet die Abfolge der Prozessschritte des Simplex-Algorithmus. Die anderen Klassen in 
 * diesem Paket implementieren wiederum die einzelnen Prozessschritte.
 * 
 * @author Wolfgang Bongartz
 */
package solver;
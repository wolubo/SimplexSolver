/**
 * Das Package beinhaltet Klassen, mit denen ein Prozess aufgebaut werden kann, der sich schrittweise durchlaufen lässt.
 * <p>
 * Die meisten mit einem Computer simulierbaren Prozesse bestehen aus einzelnen Prozessschritten, die in einer definierten 
 * Reihenfolge durchlaufen werden müssen. Die Grundidee hinter den Klassen in diesem Paket ist es, diese Prozessschritte 
 * jeweils als separate Klasse zu implementieren und die Ablaufsteuerung in eine zentrale Klasse auszulagern. So lassen 
 * sich Prozesse flexibel aus einzelnen Komponenten zusammensetzen und schrittweise durchführen. 
 * Die Prozess-Steuerung übernimmt eine Instanz der Klasse ProcessMaster. Sie stellt Methoden bereit, mit denen sich der
 * Prozessablauf definieren lässt und sie kontrolliert die Durchführung des Ablaufs. Dabei kann der Ablauf entweder 
 * schrittweise oder im Ganzen erfolgen. Außerdem verwaltet die Klasse auch den aktuellen Zustand des Prozesses. Nach der 
 * Durchführung jedes einzelnen Prozessschrittes und bei jeder Änderung des Prozesszustandes wird ein Event versendet. 
 * Diese Events werden im Programm verwendet, um den Aktivierungszustand der Buttons in der GUI zu steuern und um das 
 * Ergebnis des letzten durchgeführten Simplex-Schrittes auszugeben. 
 * Alle Klassen, die einen konkreten Prozessschritt implementieren, müssen das Interface ProcessStep erfüllen.
 * 
 * @author Wolfgang Bongartz
 */
package de.wbongartz.simplex_solver.process;
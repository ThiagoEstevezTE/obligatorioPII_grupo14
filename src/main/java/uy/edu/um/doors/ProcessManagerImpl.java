package uy.edu.um.doors;
import uy.edu.um.entities.*;

import uy.edu.um.entities.Process;
import uy.edu.um.tad.heap.EmptyHeapException;
import uy.edu.um.tad.heap.MyHeapImpl;

import uy.edu.um.tad.queue.MyQueueImpl;
import uy.edu.um.tad.queue.EmptyQueueException;
import uy.edu.um.tad.stack.EmptyStackException;
import uy.edu.um.tad.stack.MyStackImpl;

import uy.edu.um.tad.list.MyLinkedListImpl;
import uy.edu.um.tad.hash.MyHashImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessManagerImpl implements ProcessManager{

    //EL DISEÑO DE LA ESTRUCTURA DE ALMACENAMIENTO DEBE IMPLEMENTARSE EN ESTA CLASE EN RELACIÓN CON LAS ENTIDADES QUE DEFINA

    private MyQueueImpl<Process> newProcesses;
    private MyHeapImpl<Process> pendingProcesses;
    private MyStackImpl<Process> finishedProcesses;
    private Process runningProcess;
    private MyHashImpl<Integer, User> users;
    private MyLinkedListImpl<Process> allProcesses;
    private BufferedWriter logWriter;

    public ProcessManagerImpl() {
        this.newProcesses      = new MyQueueImpl<>();
        this.pendingProcesses  = new MyHeapImpl<>(false);
        this.finishedProcesses = new MyStackImpl<>();
        this.runningProcess    = null;
        this.users             = new MyHashImpl<>();
        this.allProcesses      = new MyLinkedListImpl<>();
    }


    @Override
    public void loadProcessAndUserData(String processCsvPath, String usersCsvPath) {
        System.out.println("IMPLEMENTAR");
        // Todo
    }

    @Override
    public void prepareProcesses() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void executeNextProcess() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void finishProcessOk() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void finishProcessError() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void terminateProcess(int uid) {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatus() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatusVerbose() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatusByUser(int uid) {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatusByProcess(int pid) {
        System.out.println("IMPLEMENTAR");
    }
}

package uy.edu.um.doors;
import uy.edu.um.doors.ProcessManager;
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

public class ProcessManagerImpl implements ProcessManager {

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
        String logName = "DOORS_PROCESS_LOG_" + LocalDate.now().toString() + ".txt";
        try {
            logWriter = new BufferedWriter(new FileWriter(logName, true));
        } catch (IOException e) {
            System.out.println("Error al crear log: " + e.getMessage());
            return;
        }


        try (BufferedReader br = new BufferedReader(new FileReader(usersCsvPath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(";");
                int uid        = Integer.parseInt(parts[0].trim());
                String alias   = parts[1].trim();
                UserType type  = UserType.valueOf(parts[2].trim());
                users.put(uid, new User(uid, alias, type));
            }
        } catch (IOException e) {
            System.out.println("Error leyendo usuarios: " + e.getMessage());
            return;
        }


        try (BufferedReader br = new BufferedReader(new FileReader(processCsvPath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                int s1 = line.indexOf(';');
                int s2 = line.indexOf(';', s1 + 1);
                int s3 = line.indexOf(';', s2 + 1);

                int pid      = Integer.parseInt(line.substring(0, s1).trim());
                int uid      = Integer.parseInt(line.substring(s1 + 1, s2).trim());
                String name  = line.substring(s2 + 1, s3).trim();
                String eventsRaw = line.substring(s3 + 1).trim();

                User owner = users.get(uid);
                if (owner == null) continue;

                Process p = new Process(pid, name, owner);
                parseEvents(eventsRaw, p);
                newProcesses.enqueue(p);
                allProcesses.add(p);
            }
        } catch (IOException e) {
            System.out.println("Error leyendo procesos: " + e.getMessage());
        }

        System.out.println("Carga OK - usuarios: " + users.size() + " procesos: " + allProcesses.size());
    }

    private void parseEvents(String raw, Process process) {
        raw = raw.replace("{", "").replace("}", "").trim();
        String[] eventParts = raw.split("#");
        for (String part : eventParts) {
            part = part.trim();
            int colonIdx   = part.indexOf(':');
            EventType type = EventType.valueOf(part.substring(0, colonIdx).trim());
            Event event    = new Event(type);
            String instrRaw = part.substring(colonIdx + 1).replace("[", "").replace("]", "").trim();
            for (String instr : instrRaw.split(",")) {
                String clean = instr.trim();
                if (!clean.isEmpty()) event.addInstruction(clean);
            }
            process.addEvent(event);
        }
    }

    @Override
    public void prepareProcesses() {
        if (newProcesses.isEmpty()) {
            System.out.println("No hay procesos nuevos para preparar.");
            return;
        }

        while (!newProcesses.isEmpty()) {
            try {
                Process p = newProcesses.dequeue();

                int nCpu  = p.countEvents(EventType.CPU);
                int nRam  = p.countEvents(EventType.RAM);
                int nDisk = p.countEvents(EventType.DISK);
                int total = p.getEvents().size();

                int priority = 0;
                if (total > 0) {
                    priority = (int)((8.0 * nCpu + 2.0 * nRam + 2.0 * nDisk) / total)
                            + p.getOwner().getWeight() * total;
                }

                p.setPriority(priority);
                p.setState(ProcessState.PENDING);
                pendingProcesses.insert(p);

                String msg = "[" + now() + "]: NEW PENDING PROCESS: PID=" + p.getPid()
                        + " | " + p.getName()
                        + " | " + p.getOwner().toString()
                        + " | P=" + priority;
                System.out.println(msg);
                writeLog(msg);

            } catch (EmptyQueueException e) {
                break;
            }
        }
    }

    @Override
    public void executeNextProcess() {
        if (runningProcess != null) {
            System.out.println("Ya hay un proceso en ejecución: PID=" + runningProcess.getPid());
            return;
        }
        if (pendingProcesses.isEmpty()) {
            System.out.println("No hay procesos pendientes para ejecutar.");
            return;
        }

        try {
            runningProcess = pendingProcesses.remove();
            runningProcess.setState(ProcessState.RUNNING);

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(now()).append("]: EXECUTING PROCESS: PID=")
                    .append(runningProcess.getPid())
                    .append(" | USER:").append(runningProcess.getOwner().getAlias())
                    .append(" UID:").append(runningProcess.getOwner().getUid());

            MyLinkedListImpl<Event> events = runningProcess.getEvents();
            for (int i = 0; i < events.size(); i++) {
                Event e = events.get(i);
                sb.append("\n EVENT: ").append(e.getType()).append(" | Instructions [");
                MyLinkedListImpl<String> instrs = e.getInstructions();
                for (int j = 0; j < instrs.size(); j++) {
                    sb.append(instrs.get(j));
                    if (j < instrs.size() - 1) sb.append(", ");
                }
                sb.append("]");
            }

            String msg = sb.toString();
            System.out.println(msg);
            writeLog(msg);

        } catch (EmptyHeapException e) {
            System.out.println("Error al obtener proceso pendiente.");
        }
    }

    @Override
    public void finishProcessOk() {
        if (runningProcess == null) {
            System.out.println("No hay proceso en ejecución.");
            return;
        }

        runningProcess.setState(ProcessState.FINISHED);

        String msg = "[" + now() + "]: ENDING PROCESS: PID=" + runningProcess.getPid() + " | STATE: OK";
        System.out.println(msg);
        writeLog(msg);

        finishedProcesses.push(runningProcess);
        runningProcess = null;
    }

    @Override
    public void finishProcessError() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void terminateProcess(int uid) {
        User terminator = users.get(uid);
        if (terminator == null) {
            System.out.println("No existe usuario con UID=" + uid);
            return;
        }
        finishCurrent(FinishState.TERMINATED, terminator);
    }

    private void finishCurrent(FinishState type, User terminator) {
        if (runningProcess == null) {
            System.out.println("No hay proceso en ejecución.");
            return;
        }

        Process p = runningProcess;

        if (type == FinishState.TERMINATED) {
            p.terminate(terminator);
        } else {
            p.finish(type);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(now()).append("]: ENDING PROCESS: PID=").append(p.getPid())
                .append(" | STATE: ").append(type);
        if (type == FinishState.TERMINATED && terminator != null) {
            sb.append(" by ").append(terminator.toString());
        }
        String msg = sb.toString();
        System.out.println(msg);
        writeLog(msg);


       if (finishedProcesses.size() == MAX_FINISHED_PROCESS_ON_RAM) {
           dumpFinishedStack();
       }

        finishedProcesses.push(p);
        runningProcess = null;
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

private String now() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
}

private void writeLog(String message) {
    if (logWriter == null) return;
    try {
        logWriter.write(message);
        logWriter.newLine();
        logWriter.flush();
    } catch (IOException e) {
        System.out.println("Error escribiendo log: " + e.getMessage());
    }
}
    private void dumpFinishedStack() {
        String msg = "[" + now() + "]: Finished process stack overflow";
        System.out.println(msg);
        writeLog(msg);

        while (!finishedProcesses.isEmpty()) {
            try {
                Process p = finishedProcesses.pop();

                String line = p.toFinishedString();
                System.out.println(line);
                writeLog(line);

            } catch (EmptyStackException e) {
                break;
            }
        }
    }

}
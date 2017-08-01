#!/bin/bash
case "$1" in
		start)
            nohup java -jar server.jar $2 > server.log 2>&1 &
			echo $! > .server.pid
			echo "Servidor iniciado"
            ;;
         
        stop)
            if [ -e ".server.pid" ]; then
				kill -9 `cat .server.pid`
			rm .server.pid
			echo "Servidor encerrado!"
			fi 
            ;;
        *)
            echo $"Uso: $0 {start [porta]|stop}"
            exit 1
 
esac
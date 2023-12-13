setup:
	curl -sS https://webinstall.dev/watchexec | bash

start:
	watchexec -r -w src/main/** --exts java,js,css,html -- gradle run
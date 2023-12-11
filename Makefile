setup:
	curl -sS https://webinstall.dev/watchexec | bash

start:
	watchexec -r --exts java -- gradle run
setup:
	curl -sS https://webinstall.dev/watchexec | bash
	go install github.com/pressly/goose/v3/cmd/goose@latest

start:
	watchexec -r -w src/main \
		--exts java,js,css,html \
		-i '**/java/web/node_modules/**/*' \
		--  ./gradlew run

functional-test:
	make start
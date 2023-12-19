setup:
	curl -sS https://webinstall.dev/watchexec | bash
	go install github.com/pressly/goose/v3/cmd/goose@latest
	@echo $(INFO) "Make sure to op project..."


start:
	watchexec -r -w src/main \
		--exts java,js,css,html \
		-i '**/java/web/node_modules/**/*' \
		--  ./gradlew run
lint:
	./gradlew task spotlessApply

test:
	./gradlew check

functional-test:
	make start
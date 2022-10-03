package ottosch;

import java.io.Console;

public class Main {
	public static boolean testnet = false;
	public static boolean allOptions = false;

	public static void main(String[] args) {
		parseCliArgs(args);

		Console console = System.console();
		System.out.println("Enter your wallet info:");
		System.out.println();
		
		JSONEncoder json = new JSONEncoder()
		.setMnemonic(console.readLine("Seed words: "))
		.setPassphrase(console.readPassword("Passphrase (won't be echoed): "));
		
		char[] apiKey = console.readPassword("Dojo API key - leave blank if not using dojo (won't be echoed): ");
		
		if (apiKey.length > 0) {
			json.setApiKey(apiKey)
			.setDojoUrl(console.readLine("Dojo URL: "));
		}

		if (allOptions) {
			json.setType(console.readLine("Type [whirlpool.gui]: "))
			.setVersion(console.readLine("Version [3.0.0]: "))
			.setNetwork(console.readLine("Network [mainnet]: "));
		} else {
			json.setTestnet(testnet);
		}
		
		System.out.println();
		System.out.println(json.generate());
	}

	public static void parseCliArgs(String[] args) {
		for (String token : args) {
			if (token.matches("--?(help|h)")) {
				System.out.println("Options:");
				System.out.println("-h, --help:\tshow help and exit");
				System.out.println("-t, --testnet:\tfor testnet pairing");
				System.out.println("-a, --all:\tshow all options. Used in case whirlpool version changes");
				System.out.println();
				System.exit(0);
			}
		}

		for (String token : args) {
			if (token.matches("--?(all|a)")) {
				System.out.println("Showing all options");
				allOptions = true;
				return;
			}
		}

		for (String token : args) {
			if (token.matches("--?(testnet|t)")) {
				System.out.println("Network: testnet");
				testnet = true;
				return;
			}
		}
		
		if (args.length > 0) {
			System.out.printf("Unrecognized option(s): %s\n", String.join(" ", args));
		}
	}
}

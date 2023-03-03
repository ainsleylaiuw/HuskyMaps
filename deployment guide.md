# Deploying Husky Maps to the web

Before you follow these instructions, you'll probably want to make sure that your Husky Maps project can show the map images, which requires a free MapBox account. See the optional directions in my comment under the project setup.

An easy way to deploy apps to the web is by distributing them as a JAR: a file that bundles all of your code so that it can run on anyone else's machine even without installing IntelliJ and going through our project setup instructions. Our project is already configured to make it easy for you to create a JAR that runs on Heroku.

## Bundling your program so that it can run anywhere

1. Open IntelliJ.

2. From the **File** menu, select **Project Structure** and navigate to the **Artifacts** submenu.

3. Use the ➖ button to deleted the **project:jar** — we'll need to reconfigure it from scratch.

4. Use the ➕ button to create a new **JAR from modules with dependencies**. Under **Main Class**, type `huskymaps.MapServer` and click OK.

5. We'll need to customize the **Output Layout** to include the dataset files. Add a 📁 folder called "data", and inside of it create another 📁 folder "huskymaps". Finally, use the ➕ button to add the `places.tsv.gz` and `seattle-small.osm.gz` files.

6. From the **Build** menu, select **Build Artifacts** and build the **project:jar**. This will create your project.jar file in the `out/artifacts/project_jar` directory containing all the code needed to run the Husky Maps web app.

Test your JAR by running it from the terminal. In IntelliJ, [open the terminal](https://www.jetbrains.com/help/idea/terminal-emulator.html#open-terminal), and run the following command. If everything works, you should see the Javalin welcome message. \
`PORT=8080 java -jar out/artifacts/project_jar/project.jar`

Once you have a runnable JAR file, we need to configure Heroku so it's able to accept your JAR.

## Telling Heroku how to run your app

1. Create a free [Heroku account](https://signup.heroku.com/dc).

2. Set up [Heroku Command Line Interface](https://devcenter.heroku.com/articles/getting-started-with-java#set-up) and have the terminal open.

3. Run `heroku login` to login to Heroku from your terminal.

4. Run `heroku create huskymaps-...` where `...` is your Net ID. This will create a Heroku app with the name `huskymaps-...` visible in your [Heroku Dashboard](https://dashboard.heroku.com/apps).

5. Set the config variable for your MapBox token with `heroku config:set TOKEN=...` where `...` is your MapBox access token. You can also set `TOKEN` in the Heroku dashboard settings for your app.

Then, install the Heroku Java plugin: `heroku plugins:install java` and deploy your JAR to your `huskymaps-... app`.

## Deploying your JAR to Heroku
`heroku deploy:jar out/artifacts/project_jar/project.jar --app huskymaps-... --jdk 11`

Finally, you can visit the link in the terminal to try out the app in your browser. Your app is running on Heroku's servers and can be reached by anyone on the internet!

In the future, if you want to update the code for the app, make your changes in IntelliJ, rebuild the JAR, and then re-deploy it to Heroku.

# What goes into app deployment

We saw that there's two steps to deploying an app to a cloud provider.

The first step was to bundle all the code into a single, reproducible and reusable JAR file so that it can be easily deployed as a single unit. The fact that code can be "shipped" in this format enables apps to be deployed at scale:

- When you're running a demo app like this where you're the main user, the cloud provider doesn't need to dedicate a server to your app. It can run many apps on the same server to save costs, and even turn off your app after no one uses it for a while.

- If your app goes viral and hundreds or thousands of people load your app at the same time, the cloud provider can auto-scale the service by adding more servers to run the same exact JAR file and handle more requests.

The second step was telling Heroku how to run your app. And, in the final step, we deployed the bundled JAR file over to Heroku using the deploy:jar command.
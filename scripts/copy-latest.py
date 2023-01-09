# Script to copy over the latest "numbered" version to the build directory
# Must be called AFTER the website being built by "npm run install-build"
# It will create a new directory with the numbered version, e.g. 1.32.0.Final with the contents of the "latest" directory.
# We do this way because the GH Pages can't have redirection/rewrite rules applied like suggested on Antora pages: https://docs.antora.org/antora/latest/playbook/urls-redirect-facility/
# See JIRA: https://issues.redhat.com/browse/KOGITO-8408
import re
import shutil
from pathlib import Path

pattern = re.compile("(data-version=\")((\d+\.)(\d+\.)(\*|\d+)\.Final)")
found = False
base_path = "build/site/serverlessworkflow"
index_file = Path(base_path + "/latest/index.html").resolve()

for i, line in enumerate(open(index_file)):
    for match in re.finditer(pattern, line):
        found = True
        if len(match.groups()) > 2:
            latest_version = match.groups()[1]
            print("Found latest version " +
                  latest_version + ". Copying directory.")
            dest_dir = Path(base_path + "/" + latest_version)
            if not dest_dir.is_dir():
                shutil.copytree(Path(base_path + "/latest").resolve(),
                                dest_dir.resolve())
            else:
                print("Directory with version " + latest_version +
                      " already exists. Skipping copy.")
        else:
            raise ValueError(
                "Couldn't find a match for latest docs version. Matches found based on the current regexp: " + match)
        break
    if found:
        break

if not found:
    raise ValueError("Couldn't find a match for the latest docs version in the " +
                     index_file + ". Directory not copied.")

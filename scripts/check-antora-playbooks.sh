#!/usr/bin/env bash
# check if the antora-playbook.yml and antora-playbook-author.yml are synced
# example of usage:
# sh scripts/check-antora-playbooks.sh
# 6c6
# < java_min_version: 11+
# ---
# > java_min_version: 11+s
#
# < is antora-playbook.yml
# > is antora-playbook-author.yml
set -e

ANTORA_PLAYBOOK_ATTRIBUTES=$(python -c 'import yaml; antora = open("antora-playbook.yml"); parsed_antora = yaml.load(antora, Loader=yaml.FullLoader); print(yaml.dump(parsed_antora["asciidoc"]["attributes"]))')
ANTORA_PLAYBOOK_AUTHOR_ATTRIBUTES=$(python -c 'import yaml; antora = open("antora-playbook-author.yml"); parsed_antora = yaml.load(antora, Loader=yaml.FullLoader); print(yaml.dump(parsed_antora["asciidoc"]["attributes"]))')

diff  <(echo "$ANTORA_PLAYBOOK_ATTRIBUTES")  <(echo "$ANTORA_PLAYBOOK_AUTHOR_ATTRIBUTES")

